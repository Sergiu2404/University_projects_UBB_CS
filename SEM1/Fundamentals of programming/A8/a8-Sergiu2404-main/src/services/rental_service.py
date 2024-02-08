from datetime import datetime

from domain.rental import Rental


class RentalService:
    def __init__(self, rental_repo):
        self.__rental_repo = rental_repo

    def generate10_rentals(self):
        '''
        generate 10 random rentals
        :return:
        '''
        rental1 = Rental(1, 1, 1, "11/12/22", "12/12/22", "20/12/22")
        self.__rental_repo.add_rental(rental1)

        rental2 = Rental(2, 2, 2, "11/12/22", "12/12/22", "20/12/22")
        self.__rental_repo.add_rental(rental2)

        rental3 = Rental(3, 3, 3, "11/12/22", "12/12/22", "20/12/22")
        self.__rental_repo.add_rental(rental3)

        rental4 = Rental(4, 4, 4, "11/12/22", "12/12/22",
                         str(datetime.now().day) + "/" + str(datetime.now().month) + "/" + str(datetime.now().year))
        self.__rental_repo.add_rental(rental4)

        rental5 = Rental(5, 5, 5, "11/12/22", "12/12/22", "20/12/22")
        self.__rental_repo.add_rental(rental5)

        rental6 = Rental(6, 6, 6, "11/12/22", "12/12/22",
                         str(datetime.now().day) + "/" + str(datetime.now().month) + "/" + str(datetime.now().year))
        self.__rental_repo.add_rental(rental6)

        rental7 = Rental(7, 7, 7, "11/12/22", "12/12/22",
                         str(datetime.now().day) + "/" + str(datetime.now().month) + "/" + str(datetime.now().year))
        self.__rental_repo.add_rental(rental7)

        rental8 = Rental(8, 8, 8, "11/12/22", "12/12/22", "20/12/22")
        self.__rental_repo.add_rental(rental8)

        rental9 = Rental(9, 9, 9, "11/12/22", "12/12/22", "20/12/22")
        self.__rental_repo.add_rental(rental9)

        rental10 = Rental(10, 10, 10, "11/12/22", "12/12/22",
                          str(datetime.now().day) + "/" + str(datetime.now().month) + "/" + str(
                              datetime.now().year % 100))
        self.__rental_repo.add_rental(rental10)

    def add_rental(self, id_rental, id_client, id_movie, rented_date, due_date, returned_date):
        '''
        add rental service
        :param id_rental:
        :param id_client:
        :param id_movie:
        :param rented_date:
        :param due_date:
        :param returned_date:
        :return:
        '''
        rental = Rental(id_rental, id_client, id_movie, rented_date, due_date, returned_date)
        return self.__rental_repo.add_rental(rental)

    def delete_rental_id_Rental(self, id):
        '''
        delete rental
        :param id:
        :return:
        '''
        return self.__rental_repo.delete_rental_Rental(id)

    def delete_rental_id_movie(self, id):
        '''
        delete rental by id of movie
        :param id:
        :return:
        '''
        return self.__rental_repo.delete_rental_movie(id)

    def update_rental(self, rental_id, rented_date, due_date, returned_date):
        '''
        update rental fields
        :param rental_id:
        :param rented_date:
        :param due_date:
        :param returned_date:
        :return:
        '''
        return self.__rental_repo.update_rental(rental_id, rented_date, due_date, returned_date)

    def list_rental(self):
        '''
        prints rentals
        :return:
        '''
        return self.__rental_repo.list_rental()

    def client_stats(self):
        return self.__rental_repo.clients_stats()

    def movie_stats(self):
        return self.__rental_repo.movies_stats()

    def rental_stats(self):
        return self.__rental_repo.rentals_stats()