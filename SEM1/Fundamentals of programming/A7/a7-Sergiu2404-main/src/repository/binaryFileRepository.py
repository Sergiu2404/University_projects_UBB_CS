from repository import Repository
import pickle
import os

class BinaryFileRepository(Repository):
    def __init__(self):
        super().__init__()
        self.file_location="C:\\Users\\Sergiu\\Desktop\\FP\\HW-A7\\binaryrepo.txt"
        self.read()

    def save(self, student):
        super().save(student)
        with open(self.file_location,"wb") as f:
            pickle.dump(self.show(), f)
            f.close()

    def read(self):
        with open(self.file_location,"rb") as f2:
            if os.path.getsize("C:\\Users\\Sergiu\\Desktop\\FP\\HW-A7\\binaryrepo.txt") > 0:
                self.__all_students=pickle.load(f2)
            f2.close()


