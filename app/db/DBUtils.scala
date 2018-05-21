package db

import java.sql.PreparedStatement

import play.api.db.Databases

object DBUtils {
  private val db = Databases.inMemory(name = "play", Map.empty, Map("username" -> "sa", "password" -> ""))

  val connection = db.getConnection()

  //FIXME: in real application, seed data should sit in DB server, created helper method to facilitate application running
  def supplySeedData: Unit = {
    // create tables
    runQuery("DROP TABLE WALLET IF EXISTS")
      .execute()
    runQuery("DROP TABLE PRODUCTSALE IF EXISTS")
      .execute()
    runQuery(
      "CREATE TABLE PRODUCTSALE ( ID INT NOT NULL, PRICE DOUBLE, CURRENCY VARCHAR, TOTAL_ITEMS INT, ITEMS_LEFT INT, END_TIME BIGINT, CTY VARCHAR)"
    ).execute()
    runQuery("CREATE TABLE WALLET ( id INT NOT NULL, BALANCE DOUBLE, CURRENCY VARCHAR)")
      .execute()

    // wallet data
    runQuery("INSERT INTO WALLET VALUES (1, 30.0, 'SGD') ").executeUpdate()
    runQuery("INSERT INTO WALLET VALUES (2, 900.0, 'USD') ").executeUpdate()
    runQuery("INSERT INTO WALLET VALUES (3, 50.0, 'SGD') ").executeUpdate()
    runQuery("INSERT INTO WALLET VALUES (5, 30.0, 'SGD') ").executeUpdate()

    // product sale data
    runQuery("INSERT INTO PRODUCTSALE VALUES (1, 5.0, 'SGD', 1000, 400, 1536894538, 'SG')").executeUpdate()
    runQuery("INSERT INTO PRODUCTSALE VALUES (2, 15.0, 'SGD', 1000, 300, 1536874538, 'SG')").executeUpdate()
    runQuery("INSERT INTO PRODUCTSALE VALUES (3, 25.0, 'SGD', 1000, 200, 1536874518, 'SG')").executeUpdate()
    runQuery("INSERT INTO PRODUCTSALE VALUES (4, 25.0, 'SGD', 1000, 200, 1536874518, 'SG')").executeUpdate()
  }

  private def runQuery(qry: String): PreparedStatement = {
    connection.prepareStatement(qry)
  }
}
