
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

/**/
class registration @javax.inject.Inject() /*1.6*/(webJarsUtil: org.webjars.play.WebJarsUtil) extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {
/*3.2*/import views.html.main


Seq[Any](_display_(/*4.2*/main("Home Page", webJarsUtil)/*4.32*/ {_display_(Seq[Any](format.raw/*4.34*/("""

  """),format.raw/*6.3*/("""<link rel="stylesheet" href=""""),_display_(/*6.33*/routes/*6.39*/.Assets.versioned("stylesheets/error/registration/registration.css")),format.raw/*6.107*/("""">

  <div class="container">

    <div class="row">
    <div class="offset-2 col-md-8 borders">
      <h6>T/r</h6>
      <input type="text" placeholder="" class="form-control">
      <br>
      <h6>Ta'siri natijasida ishlovchi xodimlari dastlabki tarzda va vaqti bilan tibbiy ko'rikdan o'tkazilishi shart bo'lgan xavfli va zararli moddalar nomi.</h6>
      <input type="text" placeholder="" class="form-control">
      <br>
      <h6>Tibbiy ko'rikdan o'tish muddati.</h6>
      <input type="text" placeholder="" class="form-control">
      <br>
      <h6>Tibbiy ko'rikni o'tkazuvchi vrach-mutaxassislar.</h6>
      <input type="text" placeholder="" class="form-control">
      <br>
      <h6>Laborator va funksional tekshirishlar.</h6>
      <input type="text" placeholder="" class="form-control">
      <br>
      <h6>Ishga qo'yish uchun qarshilik ko'rsatuvchi umumiy tibbiy moneliklarga qo'shimcha tibbiy moneliklar.</h6>
      <input type="text" placeholder="" class="form-control">

      <div align="center">

      <button class="btn btn-dark">Submit</button>

      </div>

    </div>
  </div>
</div>

""")))}),format.raw/*40.2*/("""
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2020-02-06T13:08:07.056
                  SOURCE: /home/doston/IdeaProjects/Smart_Medical_SCT/app/views/registration.scala.html
                  HASH: 932d7f345354d7286ac3a8c9482e2de76882c28c
                  MATRIX: 480->5|807->50|882->54|933->78|971->108|1010->110|1040->114|1096->144|1110->150|1199->218|2340->1329
                  LINES: 19->1|22->2|25->3|28->4|28->4|28->4|30->6|30->6|30->6|30->6|64->40
                  -- GENERATED --
              */
          