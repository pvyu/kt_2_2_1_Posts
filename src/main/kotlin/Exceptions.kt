package ru.netology.posts.exceptions

class PostNotFoundException(message : String) : RuntimeException(message)
class NoSuchUserException(message : String) : RuntimeException(message)