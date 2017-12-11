package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Gets the name input from the user
        EditText inputName = (EditText) findViewById(R.id.text_input);
        String fillForm = inputName.getText().toString();
        //Figure out if the user wants whipped cream topping
        CheckBox whippedCream = (CheckBox) findViewById(R.id.whiped_cream);
        boolean hasWhippedCream = whippedCream.isChecked();
        //figures out if the user wants chocolate topping
        CheckBox chocolate = (CheckBox) findViewById(R.id.chocolate);
        boolean hasChocolate = chocolate.isChecked();
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, fillForm);
        if(fillForm.length() > 0) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT,
                    getString(R.string.order_summary_email_subject, fillForm));
            intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(this, getString(R.string.name_field_blank),
                    Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;
        if(addWhippedCream) {
            basePrice = basePrice + 1;
        }
        if(addChocolate){
            basePrice = basePrice + 2;
        }
        int price = quantity * basePrice;
        return price;
    }

    /**
     * create order summary method
     * @param price
     * @param fillForm gets useer input name
     * @param addWhippedCream is whether or not the user wants whipped cream
     * @param  addChocolate is whether or not the user wants chocolate
     * @return order summary
     */

    private String createOrderSummary(int price, boolean addWhippedCream,
                                      boolean addChocolate, String fillForm) {
        String priceMessage = getString(R.string.order_summary_name, fillForm);
        if ((addWhippedCream) && (addChocolate)) {
            if (addWhippedCream) {
                priceMessage += "\n" + getString(R.string.whipped_cream);
            } else {
                priceMessage += "";
            }
            if (addChocolate) {
                priceMessage += "\n" + getString(R.string.chocolate);
            } else {
                priceMessage += "";
            }
        }
        else {
            priceMessage += "\n" + getString(R.string.no_toppings);
        }
            priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
            priceMessage += "\n" + getString(R.string.order_summary_price, price);
            priceMessage += "\n" + getString(R.string.thank_you);
            return priceMessage;

    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, getString(R.string.more_than_100_coffees),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 0) {
            Toast.makeText(this, getString(R.string.less_than_1_coffee),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}