package io.undervolt.api.ui.widgets.prefabricated;

import io.undervolt.api.ui.widgets.*;

public class InputToggle extends Drawable {

   protected boolean checked;
   protected String labelText;

   private Toggle toggle;

   /**
    * Simple toggle label
    * @param labelText Label text
    * @param checked Is it checked?
    */
   public InputToggle(String labelText, boolean checked)
   {
      this.labelText = labelText;
      this.checked = checked;
   }

   @Override public void load()
   {
      internal = new Drawable[] {
           new Padding(
                EdgeInsets.all(5),
                new Row(
                     new Padding(
                          new EdgeInsets(0, 0, 12, 0),
                          toggle = new Toggle(checked)
                     ),
                     new Padding(
                          EdgeInsets.vertical((int)(toggle.getHeight() - 10) / 2),
                          new Text(
                               labelText,
                               10
                          )
                     )
                ).crossAxisAlign(AxisAlignment.CENTER)
           )
      };

      super.load();
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
      this.toggle.setChecked(this.checked);
   }

   public boolean isChecked() {
      return checked;
   }
}
