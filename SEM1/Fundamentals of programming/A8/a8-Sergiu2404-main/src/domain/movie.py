

class Movie:
    def __init__(self,movie_id,title,description,genre):
        self.__movie_id=movie_id
        self.__title=title
        self.__description=description
        self.__genre=genre

    def get_id(self):
        return self.__movie_id
    def get_title(self):
        return self.__title
    def get_des(self):
        return self.__description
    def get_genre(self):
        return self.__genre

    def set_id(self,id):
        self.__movie_id=id
    def set_title(self,title):
        self.__title=title
    def set_des(self, des):
        self.__description = des
    def set_genre(self, genre):
        self.__genre =genre

    def __str__(self):
        return f"movie_id={self.__movie_id},title={self.__title},description={self.__description},genre={self.__genre}"
