package com.welab.wefe.union.service.scheduler;

import com.welab.wefe.common.data.mongodb.entity.union.ImageDataSetLabeledCount;
import com.welab.wefe.common.data.mongodb.repo.ImageDataSetLabeledCountMongoReop;
import com.welab.wefe.common.data.mongodb.repo.ImageDataSetMongoReop;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.union.service.service.ImageDataSetContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @Description:
 * @author: yuxin.zhang
 * @date: 2021/11/25
 */
@Configuration
public class ImageDataSetLabelCountUpdateTask {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ImageDataSetContractService imageDataSetContractService;

    @Autowired
    private ImageDataSetMongoReop imageDataSetMongoReop;

    @Autowired
    private ImageDataSetLabeledCountMongoReop imageDataSetLabeledCountMongoReop;
    @Value("${image.dataset.label.count.update.rate}")
    private String rateString;

    @Scheduled(fixedRateString = "${image.dataset.label.count.update.rate}")
    private void startTask() {
        List<ImageDataSetLabeledCount> list = imageDataSetLabeledCountMongoReop.findAll();
        for (ImageDataSetLabeledCount imageDataSetLabeledCount :
                list) {
            if (imageDataSetLabeledCount.getUpdateTime() - System.currentTimeMillis() >= Long.parseLong(rateString)) {
                boolean isLabelCompleted = false;
                if (imageDataSetLabeledCount.getLabeledCount() >= imageDataSetLabeledCount.getSampleCount()) {
                    isLabelCompleted = true;
                }
                try {
                    imageDataSetContractService.updateLabeledCount(
                            imageDataSetLabeledCount.getDataSetId(),
                            String.valueOf(imageDataSetLabeledCount.getLabeledCount()),
                            String.valueOf(imageDataSetLabeledCount.getSampleCount()),
                            imageDataSetLabeledCount.getLabelList(),
                            isLabelCompleted ? "1" : "0");
                } catch (StatusCodeWithException e) {
                    LOG.error("update ImageDataSetLabeledCount error dataSetId: " + imageDataSetLabeledCount.getDataSetId(), e);
                }
            }
        }
    }
}