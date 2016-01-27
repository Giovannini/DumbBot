package models

case class User(name: String, slackId: String, firstName: String, lastName: String, email: String) {

  def save(userStorage: UserStorage): UserStorage = userStorage.save(this)

}
