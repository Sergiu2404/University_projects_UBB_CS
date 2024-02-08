from domain.client import Client


class ServiceClient:
    def __init__(self,clients_repo,rental_repo):
        self.__clients_repo=clients_repo
        self.__rental_repo=rental_repo


    def generate10_clients(self):
        '''
        generate 10 clients
        :return:
        '''
        client1=Client(1,"ab")
        self.__clients_repo.save(client1)

        client2 = Client(2, "b")
        self.__clients_repo.save(client2)

        client3 = Client(3, "c")
        self.__clients_repo.save(client3)

        client4 = Client(4, "d")
        self.__clients_repo.save(client4)

        client5 = Client(5, "e")
        self.__clients_repo.save(client5)

        client6 = Client(6, "f")
        self.__clients_repo.save(client6)

        client7 = Client(7, "g")
        self.__clients_repo.save(client7)

        client8 = Client(8, "h")
        self.__clients_repo.save(client8)

        client9 = Client(9, "i")
        self.__clients_repo.save(client9)

        client10 = Client(10, "j")
        self.__clients_repo.save(client10)



    def add_client(self,id,name):
        '''
        add client
        :param id:
        :param name:
        :return:
        '''
        client=Client(id,name)
        self.__clients_repo.save(client)


    def delete_client(self, id):
        '''
        delete client by id
        :param id:
        :return:
        '''
        return self.__clients_repo.delete(id)


    def update_client(self,id,name):
        '''
        update client name
        :param id:
        :param name:
        :return:
        '''
        return self.__clients_repo.update(id,name)


    def list_client(self):
        '''
        shows clients
        :return:
        '''
        return self.__clients_repo.list()


    def search_id_function(self,id):
        '''
                search by id
                :param id:
                :return:
        '''
        return self.__clients_repo.search_id(id)


    def search_name_function(self,name):
        '''
                search by name
                :param id:
                :return:
        '''
        return self.__clients_repo.search_name(name)




