package club.pigplan.piggyplanner.user.domain.model

class UsernameInvalidException : IllegalArgumentException("User name invalid")

class PasswordInvalidException : IllegalArgumentException("Password invalid")