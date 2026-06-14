'''Library Management System is a Python-based OOP project that manages books, members, borrowing, returns, fines, and reward points in a library. 
It demonstrates Python OOP concepts such as Encapsulation, Inheritance, Abstraction, Polymorphism, 
Duck Typing, Exception Handling, and Composition through a real-world library workflow.'''

from abc import ABC, abstractmethod
from datetime import datetime, timedelta


class Book:

    total_books = 0

    def __init__(self, book_id, title, author, category):
        self.book_id = book_id
        self.title = title
        self.author = author
        self.category = category

        self.__available = True
        self.__borrow_count = 0

        Book.total_books += 1

    def is_available(self):
        return self.__available

    def issue_book(self):

        if self.__available:
            self.__available = False
            self.__borrow_count += 1
            return True

        return False

    def return_book(self):
        self.__available = True

    def get_borrow_count(self):
        return self.__borrow_count

    def display(self):

        status = "Available" if self.__available else "Issued"

        print(
            f"{self.book_id} | "
            f"{self.title} | "
            f"{self.author} | "
            f"{self.category} | "
            f"{status}"
        )

    def __str__(self):
        return f"{self.title}"


class Member(ABC):

    def __init__(self, member_id, name):

        self.member_id = member_id
        self.name = name

        self.borrowed_books = {}

        self.reward_points = 0
        self.fine_amount = 0

    @abstractmethod
    def max_books_allowed(self):
        pass

    def borrow_book(self, book):

        if len(self.borrowed_books) >= self.max_books_allowed():

            print(
                f"{self.name} has reached "
                f"the borrowing limit."
            )

            return

        if book.issue_book():

            due_date = (
                datetime.now() +
                timedelta(days=7)
            )

            self.borrowed_books[book] = due_date

            print(
                f"{self.name} borrowed "
                f"'{book.title}'"
            )

            print(
                f"Due Date : "
                f"{due_date.strftime('%d-%m-%Y')}"
            )

        else:

            print(
                f"'{book.title}' "
                f"is currently unavailable."
            )

    def return_book(self, book, late_days=0):

        if book not in self.borrowed_books:
            print("Book not borrowed.")
            return

        del self.borrowed_books[book]

        book.return_book()

        fine = self.calculate_fine(late_days)

        self.fine_amount += fine

        if late_days == 0:
            self.reward_points += 10

        print(
            f"{self.name} returned "
            f"'{book.title}'"
        )

        print(
            f"Fine : ₹{fine}"
        )

    @staticmethod
    def calculate_fine(days):

        return days * 5

    def display(self):

        print(f"Member ID      : {self.member_id}")
        print(f"Name           : {self.name}")
        print(
            f"Borrow Limit   : "
            f"{self.max_books_allowed()}"
        )

        print(
            f"Reward Points  : "
            f"{self.reward_points}"
        )

        print(
            f"Fine Amount    : "
            f"₹{self.fine_amount}"
        )


class StudentMember(Member):

    def max_books_allowed(self):
        return 2


class FacultyMember(Member):

    def max_books_allowed(self):
        return 5


class PremiumMember(Member):

    def max_books_allowed(self):
        return 10


class Library:

    def __init__(self):

        self.books = []
        self.members = []

    def add_book(self, book):
        self.books.append(book)

    def add_member(self, member):
        self.members.append(member)

    def search_book(self, keyword):

        print("\nSearch Results")

        found = False

        for book in self.books:

            if (
                keyword.lower()
                in book.title.lower()
            ):

                book.display()
                found = True

        if not found:
            print("No book found.")

    def display_books(self):

        print("\nLIBRARY BOOKS\n")

        for book in self.books:
            book.display()

    def display_members(self):

        print("\nLIBRARY MEMBERS\n")

        for member in self.members:

            member.display()
            print()

    def statistics(self):

        available = 0

        for book in self.books:

            if book.is_available():
                available += 1

        issued = len(self.books) - available

        most_borrowed = max(
            self.books,
            key=lambda b:
            b.get_borrow_count()
        )

        print("\nLIBRARY STATISTICS\n")

        print(
            f"Total Books     : "
            f"{len(self.books)}"
        )

        print(
            f"Available Books : "
            f"{available}"
        )

        print(
            f"Issued Books    : "
            f"{issued}"
        )

        print(
            f"Most Borrowed   : "
            f"{most_borrowed.title}"
        )


# Duck Typing Example
class Printer:

    def print_details(self, obj):
        obj.display()


library = Library()

book1 = Book(
    101,
    "Python Programming",
    "Guido Van Rossum",
    "Programming"
)

book2 = Book(
    102,
    "Data Structures",
    "Narasimha Karumanchi",
    "Computer Science"
)

book3 = Book(
    103,
    "Operating Systems",
    "Galvin",
    "Computer Science"
)

book4 = Book(
    104,
    "DBMS",
    "Korth",
    "Database"
)

book5 = Book(
    105,
    "Computer Networks",
    "Forouzan",
    "Networking"
)

book6 = Book(
    106,
    "Machine Learning",
    "Tom Mitchell",
    "AI"
)

book7 = Book(
    107,
    "Deep Learning",
    "Ian Goodfellow",
    "AI"
)

book8 = Book(
    108,
    "Cyber Security",
    "William Stallings",
    "Security"
)

book9 = Book(
    109,
    "Cloud Computing",
    "Rajkumar Buyya",
    "Cloud"
)

book10 = Book(
    110,
    "Software Engineering",
    "Pressman",
    "Engineering"
)

library.add_book(book1)
library.add_book(book2)
library.add_book(book3)
library.add_book(book4)
library.add_book(book5)
library.add_book(book6)
library.add_book(book7)
library.add_book(book8)
library.add_book(book9)
library.add_book(book10)

student = StudentMember(
    1,
    "Rahul Sharma"
)

faculty = FacultyMember(
    2,
    "Dr. Verma"
)

premium = PremiumMember(
    3,
    "Aarav Joshi"
)

library.add_member(student)
library.add_member(faculty)
library.add_member(premium)

library.display_books()

student.borrow_book(book1)
student.borrow_book(book2)
student.borrow_book(book3)

faculty.borrow_book(book3)

premium.borrow_book(book4)
premium.borrow_book(book5)

student.return_book(
    book1,
    late_days=2
)

faculty.return_book(
    book3
)

library.search_book("Python")

printer = Printer()

print("\nDUCK TYPING DEMO\n")

printer.print_details(book2)
printer.print_details(student)

library.display_members()

library.statistics()
