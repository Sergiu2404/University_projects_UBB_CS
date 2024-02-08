import unittest

from src.domain.entities import Student
from src.repository.repository import Repository
from src.services.service import Service


class Test(unittest.TestCase):

    def setUp(self):
        self.student1 = Student(998, "x", 100)
        self.__repo = Repository()
        self.__service =Service(self.__repo)
    def test_add(self):
        student2=Student(999,"y",100)
        self.__service.add_student(student2)
        self.assertEqual(self.__repo.show()[0].get_id() , student2.get_id() )


    def tearDown(self):
        pass




