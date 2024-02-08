from repository.client_repo import ClientsRepo
from repository.movie_repo import MoviesRepo
from repository.rental_repo import RentalRepo
from services.client_service import ServiceClient
from services.movie_service import ServiceMovie
from services.rental_service import RentalService
from ui.ui import UI

if __name__=='__main__':

    clients_repo=ClientsRepo()
    movies_repo=MoviesRepo()
    rental_repo=RentalRepo(clients_repo,movies_repo)

    clients_service=ServiceClient(clients_repo,rental_repo)
    movie_service=ServiceMovie(movies_repo,rental_repo)
    rental_service=RentalService(rental_repo)

    ui=UI(clients_service,movie_service,rental_service)
    ui.run()
