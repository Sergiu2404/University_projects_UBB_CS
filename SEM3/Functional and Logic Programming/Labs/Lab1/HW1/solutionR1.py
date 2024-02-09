import sys


class Nod:
    def __init__(self, e):
        self.e = e
        self.urm = None


class Lista:
    def __init__(self):
        self.prim = None


def creareLista():
    lista = Lista()
    lista.prim = creareLista_rec()
    return lista


def creareLista_rec():
    x = int(input("x="))
    if x == 0:
        return None
    else:
        nod = Nod(x)
        nod.urm = creareLista_rec()
        return nod


def tipar(lista):
    tipar_rec(lista.prim)


def tipar_rec(nod):
    if nod is not None:
        print(nod.e)
        tipar_rec(nod.urm)


def gcd(a, b):
    if b == 0:
        return a
    return gcd(b, a % b)


def find_gcd(lista):
    return find_gcd_rec(lista.prim)


def find_gcd_rec(nod):
    if nod is None:
        return None
    if nod.urm is None:
        return nod.e
    return gcd(nod.e, find_gcd_rec(nod.urm))


# Function to insert an element at a specific position in the list
def insert_at_position(lista, value, position):
    if position == 0:
        nod = Nod(value)
        nod.urm = lista.prim
        lista.prim = nod
    else:
        lista.prim.urm = insert_at_position_rec(lista.prim.urm, value, position - 1)


def insert_at_position_rec(nod, value, position):
    if position == 0:
        new_nod = Nod(value)
        new_nod.urm = nod
        return new_nod
    nod.urm = insert_at_position_rec(nod.urm, value, position - 1)
    return nod


if __name__ == "__main__":
    try:
        list = creareLista()
        tipar(list)

        gcd_result = find_gcd(list)
        print(f"GCD of elements in the list: {gcd_result}")

        position = int(input("Enter the position to insert a new element: "))
        value = int(input("Enter the value to insert: "))
        insert_at_position(list, value, position - 1)
        print("List after insertion:")
        tipar(list)
    except Exception as err:
        print(err)

    sys.exit()
