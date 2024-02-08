

class UI:
    def __init__(self,service):
        self.__service=service

    @staticmethod
    def __menu():
        print("MENU")
        print("Choose an option: ")
        print("   ADD-add student")
        print("   LIST-display students")
        print("   FILTER-filter by group")
        print("   UNDO-undo last operation")
        print("MENU")

    def run_menu(self):
        self.__service.generate_10()
        while True:
            self.__menu()
            self.__get_input()
            self.__use_input()



    def __get_input(self):
        self.__command=input("enter command: ")

    def __use_input(self):
        if self.__command.lower()=="add":
            self.__add_student()
        elif self.__command.lower()=="list":
            self.__list()
        elif self.__command.lower()=="filter":
            self.__filter()
        elif self.__command.lower()=="undo":
            self.__undo()
        elif self.__command.lower()=="exit":
            print("Are you sure?")
            opp=input("Y/N")
            if opp.lower()=="y":
                self.exit_ui()
        else:
            print("invalid command")


    def __add_student(self):

        try:
            id1=int(input("id: "))
            name = input("name: ")
            group = int(input("group number: "))
            self.__service.add_student(id1, name, group)
        except TypeError as e:
            print("Wrong input. Try again")
        except Exception as e:
            print(f"Exception: {e}")

    def __list(self):
        try:
            student_list=self.__service.display_all()
            for student in student_list:
                print(student)
        except TypeError:
            print("Something went wrong. Please try again")
        except Exception as e:
            print(f"Exception: {e}")

    def __filter(self):
        try:
            group=int(input("enter group to filter: "))
            self.__service.filter_students(group)
        except ValueError:
            print("Wrong input. Try again")
        except Exception as e:
            print(f"Exception: {e}. Try again")

    def __undo(self):
        try:
            self.__service.undo()
        except IndexError:
            print("no operations remained")
        except Exception as e:
            print(f"Exception: {e}. Try again")


    def exit_ui(self):
        self.__service.exit_program()


