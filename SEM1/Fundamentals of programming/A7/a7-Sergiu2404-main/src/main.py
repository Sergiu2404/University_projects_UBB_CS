# This is a sample Python script.
import configparser

from src.repository.binaryFileRepository import BinaryFileRepository
from src.repository.fileRepository import FileRepository
from src.repository.repository import Repository
from src.services.service import Service
from src.ui.ui import UI

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.




# Press the green button in the gutter to run the script.
if __name__ == '__main__':

    config=configparser.RawConfigParser()
    config.read("settings.properties") #settings.properties

    if config.get("repo_op","repo") == "memory":
        repo = Repository()
    if config.get("repo_op","repo") == "binary":
        repo = BinaryFileRepository()
    if config.get("repo_op","repo") == "text":
        repo = FileRepository()

    service=Service(repo)
    ui=UI(service)
    ui.run_menu()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
