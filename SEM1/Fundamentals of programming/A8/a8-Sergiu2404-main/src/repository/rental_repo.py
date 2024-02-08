import operator



class RentalRepo:
    def __init__(self,client_repo,movie_repo):
        self.__client_repo=client_repo
        self.__movie_repo=movie_repo
        self.__rentals=[]


    def add_rental(self,rental):
        '''
        add rnetal
        :param rental:
        :return:
        '''
        self.__rentals.append(rental)
        pass
        # # for rental1 in self.__rentals:
        # #     if rental1.rental_id()==rental.rental_id():
        # #         raise DuplicateID("Duplicated ID")
        #
        # # for rental1 in self.__rentals:
        # #     if rental1.client_id()==rental.client_id():
        # #         due_date_list=rental1.due_date().strip("/")
        # #         returned_date_list = rental1.returned_date().strip("/")
        # #         if int(due_date_list[0])>int(returned_date_list[0]) or int(due_date_list[1])>int(returned_date_list[1]) or int(due_date_list[2])>int(returned_date_list[2]):
        # #             return None
        # #         if int(due_date_list[0])==int(returned_date_list[0]) and (int(due_date_list[1])>int(returned_date_list)):
        # #             return None
        # #         if int(due_date_list[0])==int(returned_date_list[0]) and int(due_date_list[1])==int(returned_date_list) and int(due_date_list[2])>int(returned_date_list[2]):
        # #             return None
        # self.__rentals.append(rental)


    def delete_rental_client(self,id):
        '''

        :param id:
        :return:
        '''
        for index in range(len(self.__rentals)):
            if id==self.__rentals[index].client_id():
                self.__rentals[index:]=self.__rentals[index+1:]
                break

    def delete_rental_movie(self, id):
        '''
        delete rental
        :param id:
        :return:
        '''
        for index in range(len(self.__rentals)):
            if id == self.__rentals[index].movie_id():
                self.__rentals[index:] = self.__rentals[index + 1:]
                break

    def update_rental(self,id,ren_date,due_date,ret_date):
        '''
        update rental
        :param id:
        :param ren_date:
        :param due_date:
        :param ret_date:
        :return:
        '''
        for rental in self.__rentals:
            if id==rental.id():
                rental.set_rented_date(ren_date)
                rental.set_due_date(due_date)
                rental.set_returned_date(ret_date)

    def list_rental(self):
        '''
        shows rentals
        :return:
        '''
        return self.__rentals


    def clients_stats(self):
        '''
        show stats
        :return:
        '''
        frequency={}
        list=[]
        for rental in self.__rentals:
            frequency[str(rental.client_id())]+=1
        sorted_frequency = sorted(frequency,key=frequency.get,reverse=True) ###sorts dictionary by value and returns list

        for id in sorted_frequency:
            for client in self.__client_repo.__clients:
                if int(id)==client.get_id(): #getting the clients by id
                    list.append(client)

        return list


    def movies_stats(self):
        pass


    def rentals_stats(self):
        pass