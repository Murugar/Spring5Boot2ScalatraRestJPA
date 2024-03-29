package com.iqmsoft

import java.sql.ResultSet
import javax.sql.DataSource

import org.json4s.{DefaultFormats, Formats}
import org.psnively.scala.jdbc.core.JdbcTemplate
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository

object Application {
  def main(args: Array[String]) {
    SpringApplication.run(classOf[Application], args:_*)
  }
}

@SpringBootApplication
class Application {

  @Bean
  def customServletRegistrationBean(customServlet : CustomServlet) = new ServletRegistrationBean(customServlet, "/*")

  @Bean
  def customServletTest(dataSource : DataSource) = new CustomServlet(dataSource)

}

@Repository
class CustomServlet(dataSource : DataSource) extends ScalatraServlet with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  private val jdbcTemplate = new JdbcTemplate(dataSource)

  private val ALL_HOTELS = "SELECT id, name FROM hotels"

  private val MAP_HOTELS = (rs : ResultSet, i : Int) => Hotel(rs.getInt(1), rs.getString(2))

  get("/") {
    "index page, SPA?"
  }

  get("/api/hotels") {
    contentType = formats("json")
    jdbcTemplate.queryAndMap(ALL_HOTELS)(MAP_HOTELS)
  }

}

case class Hotel(id : Int, name : String)
