import os.path

from service.service_action import BaseServiceAction


class JavaServiceAction(BaseServiceAction):

    def run(self):
        download_path = self.download(self.action_info.service + ".sql")

        target = os.path.join(
            self.workspace,
            self.action_info.service + ".jar"
        )
        self.replace_file(download_path, target)
        pass
