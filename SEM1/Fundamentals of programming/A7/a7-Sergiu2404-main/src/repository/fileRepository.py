
from repository import Repository
import os

from src.domain.entities import Student


class FileRepository(Repository):
    def __init__(self):
        super().__init__()
        self.__file_name = "C:\\Users\\Sergiu\\Desktop\\FP\\HW-A7\\filerepo.txt"
        self.__load_data()

    def __load_data(self):
        with open(self.__file_name) as f:
            if os.path.getsize(self.__file_name) > 0:
                for line in f:
                    array = line.split(",")
                    student = Student(int(array[0]), array[1], int(array[2]))
                    super().save(student)

    def save(self,student):
        super().save(student)
        with open(self.__file_name, "a") as f: #appends at the end of the file
            f.write(str(student.get_id()) + "," + student.get_name() + "," + str(student.get_group())+"\n")