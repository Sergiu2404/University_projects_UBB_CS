

class Repository:
    def __init__(self):
        self.__all_students=[]
        self.__history=[]


    def show(self):
        '''
        returns list with students
        '''
        return self.__all_students


    def save(self,student):
        '''
        adds student to list and replaces the previous list
        :param student:
        :return:
        '''
        try:
            if self.find_by_id(student.get_id()) is not None:
                raise ValueError("Duplicate ID")
            self.__history.append(self.__all_students[:])
            self.__all_students.append(student)
        except ValueError as err:
            print(f"Exception: {err}")


    def find_by_id(self,id):
        '''
        search by id in students list
        :param id:
        :return:
        '''
        for student in self.__all_students:
            if student.get_id() == id:
                return student
        return None

    def filter(self,group):
        '''
        filter students list by group
        :param group:
        :return:
        '''
        self.__history.append(self.__all_students[:])
        self.__all_students=[student for student in self.__all_students if student.get_group()!=group]



    def undo(self):
        self.__all_students[:] = self.__history[-1]
        self.__history.pop()

    # def delete(self,student):
    #     del self.__all_students[student.get_group()]








