import datetime
from dataclasses import dataclass

#from domain.clients import Client
#from domain.movie import Movie


@dataclass
class Rental:
    rental_id: int
    client_id: int
    movie_id: int
    rented_date: str
    due_date: str
    returned_date: str
    # def __init__(self,rental_id: int,client_id: int,movie_id: int,rented_date: str,due_date: str,returned_date: str):
    #     self.__rental_id = rental_id
    #     self.__client_id = client_id
    #     self.__movie_id = movie_id
    #     self.__rented_date = rented_date
    #     self.__due_date = due_date
    #     self.__returned_date = returned_date

    def rental_id(self):
        return self.rental_id
    def client_id(self):
        return self.client_id
    def movie_id(self):
        return self.movie_id
    def rented_date(self):
        return self.rented_date
    def due_date(self):
        return self.due_date
    def returned_date(self):
        return self.returned_date

    def set_rented_date(self,value):
        self.rented_date=value
    def set_due_date(self,value):
        self.due_date=value
    def set_returned_date(self,value):
        self.returned_date=value


    def __repr__(self):
        return f"rental_id: {self.rental_id},client_id: {self.client_id},movie_id: {self.movie_id},rented_date: {self.rented_date},due_date: {self.due_date},returned_date: {self.returned_date}"

