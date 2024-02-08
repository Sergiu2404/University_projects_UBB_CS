

class Client:
    def __init__(self,client_id,name):
        self.__client_id=client_id
        self.__name = name

    def get_id(self):
        return self.__client_id
    def get_name(self):
        return self.__name

    def set_id(self,id):
        self.__client_id=id
    def set_name(self,name):
        self.__name=name

    def __str__(self):
        return f"client_id={self.__client_id}, name={self.__name}"