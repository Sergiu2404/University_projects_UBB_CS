from exceptions import DuplicateID, IDNotFound


class ClientsRepo:
    def __init__(self):
        self.__clients=[]


    def save(self, client):
        '''
        add client to the list
        '''
        for client1 in self.__clients:
            if client1.get_id()==client.get_id():
                raise DuplicateID("\033[1m"+"\n Duplicated ID \n"+"\033[0m")
        self.__clients.append(client)

    def delete(self,id_delete):
        '''
        delete client from the list by id
        '''
        for index in range(len(self.__clients)):
            if id_delete==self.__clients[index].get_id():
                self.__clients[index:]=self.__clients[index+1:]
                break
        raise IDNotFound("ID does not exist")

    def delete_test(self,id_delete):
        '''
        delete by id
        :param id_delete:
        :return:
        '''
        for index in range(len(self.__clients)):
            if id_delete==self.__clients[index].get_id():
                self.__clients[index:]=self.__clients[index+1:]
                break


    # def find_by_id(self,id):
    #
    #     for client in self.__clients:
    #         if id==client.get_id():
    #             return client
    #     return None

    def update(self,id_update,name):
        '''
        update name of the client with id_update id
        :param id_update:
        :param name:
        :return:
        '''
        for client in self.__clients:
            if id_update==client.get_id():
                client.set_name(name)


    def list(self):
        '''
        display clients
        :return:
        '''
        return self.__clients


    def search_id(self,id):
        '''
        search clients by id
        :param id:
        :return:
        '''
        for client in self.__clients:
            if id==client.get_id():
                return client

        return -1


    def search_name(self,name):
        '''
        search client by partial name matching
        :param name:
        :return:
        '''
        list=[]
        for client in self.__clients:
            if name.lower() in client.get_name().lower():
                list.append(client)

            #if client.get_name().find(name)
            #    list.append(client)

        return list

