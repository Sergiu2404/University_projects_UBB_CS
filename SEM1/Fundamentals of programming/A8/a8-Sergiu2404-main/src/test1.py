import unittest

from domain.client import Client
from domain.movie import Movie
from repository.client_repo import ClientsRepo
from repository.movie_repo import MoviesRepo
from repository.rental_repo import RentalRepo
from services.client_service import ServiceClient
from services.movie_service import ServiceMovie


class Test(unittest.TestCase):
    def setUp(self):
        # self.client111=Client(111,"Nume")
        # self.movie111=Movie(111,"Titlu","Des","Gen")

        self.__client_repo = ClientsRepo()
        self.__movie_repo = MoviesRepo()
        self.__rental_repo = RentalRepo(self.__client_repo,self.__movie_repo)

        self.__client_service=ServiceClient(self.__client_repo,self.__movie_repo)
        self.__movie_service=ServiceMovie(self.__movie_repo,self.__rental_repo)


    def test_add(self):
        client112 = Client(112, "Nume2")
        movie112 = Movie(112, "Titlu2", "Des", "Gen")

        self.__client_repo.save(client112)
        self.__movie_repo.save(movie112)

        self.assertEqual(len(self.__client_repo.list()), 1)
        self.assertEqual(len(self.__movie_repo.list()), 1)

        # self.__client_service.add_client(113,"Nume")
        # self.__movie_service.add_movie(113,"titlu","des","gen")
        #
        # self.assertEqual(len(self.__client_repo.list()), 2)
        # self.assertEqual(len(self.__movie_repo.list()), 2)

    def test_delete(self):

        client112 = Client(112, "Nume2")
        movie112 = Movie(112, "Titlu2", "Des", "Gen")

        client113 = Client(113, "Nume2")
        movie113 = Movie(113, "Titlu2", "Des", "Gen")

        # self.__client_repo.__clients = [client112]
        # self.__movie_repo.__movies = [movie112]

        self.__client_repo.save(client112)
        self.__movie_repo.save(movie112)

        self.__client_repo.save(client113)
        self.__movie_repo.save(movie113)

        self.__client_repo.delete_test(client113.get_id())
        self.__movie_repo.delete_test(movie113.get_id())

        self.assertEqual(len(self.__client_repo.list()), 1)
        self.assertEqual(len(self.__movie_repo.list()), 1)

    #def test_update(self):
        #pass
        #client112 = Client(112, "Nume2")
        #movie112 = Movie(112, "Titlu2", "Des", "Gen")




    def tearDown(self):
        pass

