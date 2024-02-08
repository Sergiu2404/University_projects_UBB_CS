from src.domain.entities import Student


class Service:
    def __init__(self,repository):
        self.__repository=repository

    def generate_10(self):
        '''
        10 programmatically generated entries in the application at start-up
        :return:
        '''
        student1 = Student(1, "A", 1)
        self.__repository.save(student1)
        student2 = Student(2, "A", 1)
        self.__repository.save(student2)
        student3 = Student(3, "B", 1)
        self.__repository.save(student3)
        student4 = Student(4, "B", 1)
        self.__repository.save(student4)
        student5 = Student(5, "C", 2)
        self.__repository.save(student5)
        student6 = Student(6, "D", 2)
        self.__repository.save(student6)
        student7 = Student(7, "D", 2)
        self.__repository.save(student7)
        student8 = Student(8, "E", 2)
        self.__repository.save(student8)
        student9 = Student(9, "E", 2)
        self.__repository.save(student9)
        student10 = Student(10, "I", 2)
        self.__repository.save(student10)

    def add_student(self,id,name,group):
        '''
        add student instance to the all students list
        :param id:
        :param name:
        :param group:
        :return:
        '''
        student=Student(id,name,group)
        self.__repository.save(student)

    def display_all(self):
        '''
        display list of students
        :return:
        '''
        return self.__repository.show()

    def filter_students(self,group):
        '''
        filter students by group
        :param group:
        :return:
        '''
        return self.__repository.filter(group)

    def undo(self):
        return self.__repository.undo()

    def exit_program(self):
        exit()

