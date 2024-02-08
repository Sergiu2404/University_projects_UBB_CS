from datetime import datetime

from exceptions import DuplicateID


class UI:
    def __init__(self,service_client,service_movie,service_rental):
        self.__service_client=service_client
        self.__service_movie=service_movie
        self.__service_rental=service_rental

    def __menu(self):
        print("MENU----------")
        print("   add       -")
        print("   delete    -")
        print("   update    -")
        print("   list      -")
        print("   statistics-")
        print("MENU----------")

    def run(self):

        self.__service_client.generate10_clients()
        self.__service_movie.generate10_movies()
        self.__service_rental.generate10_rentals()

        while True:
            self.__menu()
            self.__get_input()
            self.__use_input()



    def __get_input(self):
        try:
            self.__command=input("enter option: ")
        except Exception as E:
            print(f"Exception {E}")

    def __use_input(self):
        if self.__command.lower()=="add":

            option=input("client/movie/rental: ")

            if option.lower()=="client":
                self.__add_client()
            elif option.lower()=="movie":
                self.__add_movie()
            elif option.lower()=="rental":
                self.__add_rental()

        elif self.__command.lower()=="delete":

            option = input("client/movie/rental: ")

            if option.lower() == "client":
                self.__delete_client()
            elif option.lower() == "movie":
                self.__delete_movie()
            elif option.lower()=="rental":
                self.__delete_rental()

        elif self.__command.lower()=="update":

            option = input("client/movie: ")

            if option.lower() == "client":
                self.__update_client()
            elif option.lower() == "movie":
                self.__update_movie()
            elif option.lower()=="rental":
                self.__update_rental()
            else:
                print("invalid command")

        elif self.__command.lower()=="list":

            option = input("client/movie/rental: ")

            if option.lower() == "client":
                self.__list_client()
            elif option.lower() == "movie":
                self.__list_movie()
            elif option.lower()=="rental":
                self.__list_rental()
            else:
                print("invalid command")

        elif self.__command.lower()=="search":

            option = input("client/movie: ")

            if option.lower() == "client":
                self.__search_client()
            elif option.lower() == "movie":
                self.__search_movie()
            else:
                print("invalid command")

        elif self.__command.lower()=="statistics":

            option = input("most active clients/most rented movies/late rentals(command: client/movie/rental): ")

            if option.lower() == "client":
                self.__statistics_client()
            elif option.lower() == "movie":
                self.__statistics_movie()
            elif option.lower() == "rental":
                self.__statistics_rental()

        elif self.__command.lower() == "exit":
            print("Are you sure?")
            opp = input("Y/N")
            if opp.lower() == "y":
                self.__exit_ui()
        else:
            print("invalid command")


    def __add_client(self):

        try:
            id_client = int(input("id: "))
            name = input("name: ")

            self.__service_client.add_client(id_client, name)
        except DuplicateID as ex:
            print(f"Exception: {ex}")
        except Exception as e:
            print(f"Exception: {e}")
    def __add_movie(self):

        try:
            id_movie=int(input("id: "))
            title=input("name: ")
            description = input("description: ")
            genre = input("genre: ")

            self.__service_movie.add_movie(id_movie,title,description,genre)
        except DuplicateID as ex:
            print(f"Exception: {ex}")
        except Exception as e:
            print(f"Exception: {e}")


    def __add_rental(self):

        try:
            id_rental=int(input("rental-id: "))
            id_client=input("client-id: ")
            id_movie = input("movie-id: ")
            rented_date = input("rented_date: ")
            #rented_date=datetime.strptime(rented_date,"%d/%m/%y")
            due_date=input("due date: ")
            #due_date = datetime.strptime(due_date, "%d/%m/%y")
            returned_date=input("returned date: ") #str(datetime.now().day)+"/"+str(datetime.now().month)+"/"+str(datetime.now().year%100)
            #returned_date = datetime.strptime(returned_date, "%d/%m/%y")

            self.__service_rental.add_rental(id_rental,id_client,id_movie,rented_date,due_date,returned_date)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")



    def __delete_client(self):

        try:
            id_client_delete=int(input("id: "))

            self.__service_client.delete_client(id_client_delete)
            self.__service_rental.delete_rental(id_client_delete)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __delete_movie(self):

        try:
            id_movie_delete=int(input("id: "))

            self.__service_movie.delete_movie(id_movie_delete)
            self.__service_rental.delete_rental(id_movie_delete)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __delete_rental(self):

        try:
            id_rental_delete=int(input("id: "))

            self.__service_rental.delete_rental(id_rental_delete)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __update_client(self):

        try:
            id_client_update=int(input("id: "))
            name_update=input("new name: ")

            self.__service_client.update_client(id_client_update,name_update)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __update_movie(self):

        try:
            id_movie_update=int(input("id: "))
            title_update=input("new title: ")
            des_update=input("new description: ")
            genre_update=input("new genre: ")

            self.__service_movie.update_movie(id_movie_update,title_update,des_update,genre_update)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __update_rental(self):

        try:
            rental_id=int(input("id of rental to update: "))
            rented_date=input("rented date: ")
            due_date=input("due date: ")
            returned_date=input("returned date: ")


            self.__service_rental.update_rental(rental_id,rented_date,due_date,returned_date)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __list_client(self):

        try:
            clients=self.__service_client.list_client()

            for client in clients:
                print(client)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __list_movie(self):

        try:
            movies=self.__service_movie.list_movie()

            for movie in movies:
                print(movie)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __list_rental(self):

        try:
            rentals=self.__service_rental.list_rental()

            for rental in rentals:
                print(rental)
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")


    def __search_client(self):

        try:
            option=input("id/name: ")

            if option.lower()=="id":

                id_search=int(input("search id: "))
                result=self.__service_client.search_id_function(id_search)

                if result==-1:
                    print("No client with specified ID found")
                else:
                    print(result)


            elif option.lower()=="name":

                name_search=input("search name: ")
                list=self.__service_client.search_name_function(name_search)
                for element in list:
                    print(element)
            else:
                print("wrong option. Try again")
                self.__search_client()
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")



    def __search_movie(self):

        try:
            option = input("id/title/description/genre: ")

            if option.lower() == "id":

                id_search = int(input("search id: "))
                result=self.__service_movie.search_id_function(id_search)
                if result==-1:
                    print("No movie with specified ID")
                else:
                    print(result)

            elif option.lower() == "title":
                title_search = input("search title: ")
                list=self.__service_movie.search_title_function(title_search)

                for element in list:
                    print(element)


            elif option.lower() == "description":
                des_search = input("search description: ")
                list=self.__service_movie.search_description_function(des_search)

                for element in list:
                    print(element)

            elif option.lower() == "genre":
                genre_search = input("search genre: ")
                list=self.__service_movie.search_genre_function(genre_search)

                for element in list:
                    print(element)

            else:
                print("wrong option. Try again")
                self.__search_movie()
        except TypeError as t:
            print(f"Exception: {t}")
        except ValueError as v:
            print(f"Exception: {v}")
        except Exception as e:
            print(f"Exception: {e}")



    def __statistics_client(self):
        try:
            print(self.__service_rental.client_stats())
        except Exception as E:
            print(f"Exception {E}")


    def __statistics_movie(self):
        try:
            self.__service_rental.movies_stats()
        except Exception as E:
            print(f"Exception {E}")


    def __statistics_rental(self):
        self.__service_rental.rental_stats()

    def __exit_ui(self):
        exit()
