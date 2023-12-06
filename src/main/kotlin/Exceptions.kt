package ru.netology.posts.exceptions

class PostNotFoundException(message : String) : RuntimeException(message)
class CommentNotFoundException(message : String) : RuntimeException(message)
class NoteNotFoundException(message : String) : RuntimeException(message)
class NoSuchUserException(message : String) : RuntimeException(message)
class NoUserGivenException(message : String) : RuntimeException(message)