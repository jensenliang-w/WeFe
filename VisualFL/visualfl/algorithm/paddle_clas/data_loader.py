

from __future__ import print_function

import requests
import shutil
import sys
import tarfile
import zipfile
import six.moves.cPickle as pickle
import functools
from visualfl import get_data_dir
from paddle.dataset.image import *
from paddle.reader import *
from paddle import compat as cpt
import os
from multiprocessing import cpu_count
import six
from six.moves import cPickle as pickle
from paddle.dataset.common import md5file


__all__ = ['train', 'test', 'valid']

DATA_URL = 'xxx'
DATA_MD5='XXX'

DATA_DIR = os.path.join(get_data_dir(),"flowers")
IMAGE_FILE_NAME='image.tgz'
TRAIN_LIST_FILE='train_list.txt'
TEST_LIST_FILE='test_list.txt'
VALID_LIST_FILE='val_list.txt'

TRAIN_FLAG = 'trnid'
TEST_FLAG = 'tstid'
VALID_FLAG = 'valid'


def default_mapper(is_train, sample):
    '''
    map image bytes data to type needed by model input layer
    '''
    img, label = sample
    img = load_image_bytes(img)
    img = simple_transform(
        img, 256, 224, is_train, mean=[103.94, 116.78, 123.68])
    return img.flatten().astype('float32'), label


train_mapper = functools.partial(default_mapper, True)
test_mapper = functools.partial(default_mapper, False)


def reader_creator(data_file,
                   img2label_file,
                   dataset_name,
                   mapper,
                   buffered_size=1024,
                   use_xmap=True,
                   cycle=False):
    '''
    1. read images from tar file and
        merge images into batch files in 102flowers.tgz_batch/
    2. get a reader to read sample from batch file

    :param data_file: downloaded data file
    :type data_file: string
    :param img2label_file: downloaded label file
    :type label_file: string
    :param dataset_name: data set name (trnid|tstid|valid)
    :type dataset_name: string
    :param mapper: a function to map image bytes data to type
                    needed by model input layer
    :type mapper: callable
    :param buffered_size: the size of buffer used to process images
    :type buffered_size: int
    :param cycle: whether to cycle through the dataset
    :type cycle: bool
    :return: data reader
    :rtype: callable
    '''
    img2label = {}
    for line in open(img2label_file):
        line = line.strip('\n')
        lines = line.split(' ')
        img = lines[0]
        img2label[img] = int(lines[1])
    file_list = batch_images_from_tar(data_file, dataset_name, img2label)

    def reader():
        while True:
            with open(file_list, 'r') as f_list:
                for file in f_list:
                    file = file.strip()
                    batch = None
                    with open(file, 'rb') as f:
                        if six.PY2:
                            batch = pickle.load(f)
                        else:
                            batch = pickle.load(f, encoding='bytes')

                        if six.PY3:
                            batch = cpt.to_text(batch)
                        data_batch = batch['data']
                        labels_batch = batch['label']
                        for sample, label in six.moves.zip(data_batch,
                                                           labels_batch):
                            yield sample, int(label)
            if not cycle:
                break

    if use_xmap:
        return xmap_readers(mapper, reader, min(4, cpu_count()), buffered_size)
    else:
        return map_readers(mapper, reader)


def train(data_dir=DATA_DIR,mapper=train_mapper, buffered_size=1024, use_xmap=True, cycle=False):
    '''
    Create flowers training set reader.
    It returns a reader, each sample in the reader is
    image pixels in [0, 1] and label in [1, 102]
    translated from original color image by steps:
    1. resize to 256*256
    2. random crop to 224*224
    3. flatten
    :param mapper:  a function to map sample.
    :type mapper: callable
    :param buffered_size: the size of buffer used to process images
    :type buffered_size: int
    :param cycle: whether to cycle through the dataset
    :type cycle: bool
    :return: train data reader
    :rtype: callable
    '''
    return reader_creator(
        os.path.join(data_dir, IMAGE_FILE_NAME),
        os.path.join(data_dir, TRAIN_LIST_FILE),
        TRAIN_FLAG,
        mapper,
        buffered_size,
        use_xmap,
        cycle=cycle)


def test(data_dir=DATA_DIR,mapper=test_mapper, buffered_size=1024, use_xmap=True, cycle=False):
    '''
    Create flowers test set reader.
    It returns a reader, each sample in the reader is
    image pixels in [0, 1] and label in [1, 102]
    translated from original color image by steps:
    1. resize to 256*256
    2. random crop to 224*224
    3. flatten
    :param mapper:  a function to map sample.
    :type mapper: callable
    :param buffered_size: the size of buffer used to process images
    :type buffered_size: int
    :param cycle: whether to cycle through the dataset
    :type cycle: bool
    :return: test data reader
    :rtype: callable
    '''
    return reader_creator(
        os.path.join(data_dir, IMAGE_FILE_NAME),
        os.path.join(data_dir, TEST_LIST_FILE),
        TEST_FLAG,
        mapper,
        buffered_size,
        use_xmap,
        cycle=cycle)


def valid(data_dir=DATA_DIR,mapper=test_mapper, buffered_size=1024, use_xmap=True):
    '''
    Create flowers validation set reader.
    It returns a reader, each sample in the reader is
    image pixels in [0, 1] and label in [1, 102]
    translated from original color image by steps:
    1. resize to 256*256
    2. random crop to 224*224
    3. flatten
    :param mapper:  a function to map sample.
    :type mapper: callable
    :param buffered_size: the size of buffer used to process images
    :type buffered_size: int
    :return: test data reader
    :rtype: callable
    '''
    return reader_creator(
        os.path.join(data_dir,IMAGE_FILE_NAME),
        os.path.join(data_dir, VALID_LIST_FILE),
        VALID_FLAG, mapper,
        buffered_size, use_xmap)


def download(url, module_name, save_name=None):
    dirname = os.path.join(get_data_dir(), module_name)
    if not os.path.exists(dirname):
        os.makedirs(dirname)

    filename = os.path.join(dirname,
                            url.split('/')[-1]
                            if save_name is None else save_name)

    if os.path.exists(filename):
        return filename

    retry = 0
    retry_limit = 3
    while not os.path.exists(filename):
        if retry < retry_limit:
            retry += 1
        else:
            raise RuntimeError("Cannot download {0} within retry limit {1}".
                               format(url, retry_limit))
        sys.stderr.write("Cache file %s not found, downloading %s \n" %
                         (filename, url))
        sys.stderr.write("Begin to download\n")
        r = requests.get(url, stream=True)
        total_length = r.headers.get('content-length')

        if total_length is None:
            with open(filename, 'wb') as f:
                shutil.copyfileobj(r.raw, f)
        else:
            with open(filename, 'wb') as f:
                chunk_size = 4096
                total_length = int(total_length)
                total_iter = total_length / chunk_size + 1
                log_interval = total_iter / 20 if total_iter > 20 else 1
                log_index = 0
                for data in r.iter_content(chunk_size=chunk_size):
                    if six.PY2:
                        data = six.b(data)
                    f.write(data)
                    log_index += 1
                    if log_index % log_interval == 0:
                        sys.stderr.write(".")
                    sys.stdout.flush()
    sys.stderr.write("\nDownload finished\n")
    sys.stdout.flush()

    un_zip(filename,dirname)
    return filename

def extract(tar_file, target_path):
    try:
        tar = tarfile.open(tar_file, "r:gz")
        file_names = tar.getnames()
        for file_name in file_names:
            tar.extract(file_name, target_path)
        tar.close()
    except Exception as e:
        print(e)


def un_zip(file_name,target_path):
    """unzip zip file"""
    try:
        zip_file = zipfile.ZipFile(file_name)
        if os.path.isdir(target_path):
            pass
        else:
            os.mkdir(target_path)
        for names in zip_file.namelist():
            zip_file.extract(names,target_path)
        zip_file.close()
    except Exception as e:
        print(e)


if __name__ == '__main__':
    # train()
    url = "https://xbd-dev.wolaidai.com/board-service-03//image_data_set/download?data_set_id=f7b62e3ace574b67824e8bcd42326ba7&job_id=c3e086211d2f4a288314073f3878323a"
    data_file = download(url,"","c3e086211d2f4a288314073f3878323a.zip")
    # dirname = os.path.join(get_data_dir(), "fruit1")
    # data_file = "/Users/tracy.zhang/Wefe/VisualFL/data/data_flowers/c3e086211d2f4a288314073f3878323a.zip"
    # un_zip(data_file,dirname)
    os.rename(os.path.join(get_data_dir(), "c3e086211d2f4a288314073f3878323a"), os.path.join(get_data_dir(), "fruit1"))


