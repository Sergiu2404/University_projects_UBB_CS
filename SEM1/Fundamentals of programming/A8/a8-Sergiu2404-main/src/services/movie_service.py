from domain.movie import Movie

class ServiceMovie:
    def __init__(self,movies_repo,rental_repo):
        self.__movies_repo = movies_repo
        self.__rental_repo=rental_repo


    def generate10_movies(self):
        '''
        generate 10 movies
        :return:
        '''
        movie1 = Movie(1, "aaa","...","action")
        self.__movies_repo.save(movie1)

        movie2 = Movie(2, "bbb", "...", "action")
        self.__movies_repo.save(movie2)

        movie3 = Movie(3, "ccc", "...", "action")
        self.__movies_repo.save(movie3)

        movie4 = Movie(4, "ddd", "...", "action")
        self.__movies_repo.save(movie4)

        movie5 = Movie(5, "eee", "...", "action")
        self.__movies_repo.save(movie5)

        movie6 = Movie(6, "fff", "...", "action")
        self.__movies_repo.save(movie6)

        movie7 = Movie(7, "ggg", "...", "action")
        self.__movies_repo.save(movie7)

        movie8 = Movie(8, "hhh", "...", "action")
        self.__movies_repo.save(movie8)

        movie9 = Movie(9, "iii", "...", "action")
        self.__movies_repo.save(movie9)

        movie10 = Movie(10, "jjj", "...", "action")
        self.__movies_repo.save(movie10)



    def add_movie(self,id,title,des,genre):
        '''
        add movie
        :param id:
        :param title:
        :param des:
        :param genre:
        :return:
        '''
        movie=Movie(id,title,des,genre)
        self.__movies_repo.save(movie)


    def delete_movie(self, id):
        '''
        delete movie
        :param id:
        :return:
        '''
        return self.__movies_repo.delete(id)


    def update_movie(self,id,title,des,genre):
        '''
        update movie
        :param id:
        :param title:
        :param des:
        :param genre:
        :return:
        '''
        return self.__movies_repo.update_movie(id, title,des,genre)


    def list_movie(self):
        '''
        list movies
        :return:
        '''
        return self.__movies_repo.list()


    def search_id_function(self,id):
        '''
        search by id
        :param id:
        :return:
        '''
        return self.__movies_repo.search_id(id)


    def search_title_function(self,title):
        '''
                search by title
                :param id:
                :return:
        '''
        return self.__movies_repo.search_title(title)


    def search_description_function(self,des_search):
        '''
                search by description
                :param id:
                :return:
        '''
        return self.__movies_repo.search_description(des_search)


    def search_genre_function(self,genre):
        '''
                search by genre
                :param id:
                :return:
        '''
        return self.__movies_repo.search_genre(genre)


