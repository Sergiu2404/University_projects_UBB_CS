from exceptions import DuplicateID, IDNotFound


class MoviesRepo:
    def __init__(self):
        self.__movies=[]


    def save(self, movie):
        '''
        add movie to the list of movies
        :param movie:
        :return:
        '''
        for movie1 in self.__movies:
            if movie1.get_id() == movie.get_id():
                raise DuplicateID("Duplicated ID")
        self.__movies.append(movie)


    def delete(self,id_delete):
        '''
        delete movie by id function
        :param id_delete:
        :return:
        '''
        for index in range(len(self.__movies)):
            if id_delete==self.__movies[index].get_id():
                self.__movies[index:]=self.__movies[index+1:]
                break

        raise IDNotFound("ID does not exist")

    def delete_test(self,id_delete):
        '''
        delete test function
        :param id_delete:
        :return:
        '''
        for index in range(len(self.__movies)):
            if id_delete==self.__movies[index].get_id():
                self.__movies[index:]=self.__movies[index+1:]
                break



    # def find_by_id(self,id):
    #
    #     for movie in self.__movies:
    #         if id==movie.get_id():
    #             return movie
    #     return None


    def update(self,id_update,title,des,genre):
        '''
        update movies
        :param id_update:
        :param title:
        :param des:
        :param genre:
        :return:
        '''
        for movie in self.__movies:
            if id_update==movie.get_id():
                movie.set_title(title)
                movie.set_des(des)
                movie.set_genre(genre)


    def list(self):
        '''
        display movies
        :return:
        '''
        return self.__movies


    def search_id(self,id):
        '''
                search movie by partial name matching
                :param name:
                :return:
        '''
        for movie in self.__movies:
            if id==movie.get_id():
                return movie

        return -1


    def search_title(self,title):
        '''
                search movie by partial name matching
                :param name:
                :return:
        '''
        list=[]
        for movie in self.__movies:
            if title.lower() in movie.get_title().lower():
                list.append(movie)

        return list


    def search_description(self,des):
        '''
                search movie by partial description matching
                :param name:
                :return:
        '''
        list=[]
        for movie in self.__movies:
            if des.lower() in movie.get_des().lower():
                list.append(movie)

        return list


    def search_genre(self,genre):
        '''
                search movie by partial genre matching
                :param name:
                :return:
        '''
        list=[]
        for movie in self.__movies:
            if genre.lower() in movie.get_genre().lower():
                list.append(movie)

        return list