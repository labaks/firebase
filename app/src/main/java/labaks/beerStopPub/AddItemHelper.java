package labaks.beerStopPub;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

class AddItemHelper implements ValueEventListener {

    private Long numberOfChildren;
    private Item item;
    private DatabaseReference root;

    AddItemHelper(Item item, DatabaseReference root) {
        this.item = item;
        this.root = root;
    }

    private String nameOfNextChild(long number) {
        if (number <= 100) {
            return "item0" + number;
        } else {
            return "item" + number;
        }
    }

    private void addItemOnPossible(Item item) {
        Item temp = item;
        item = null;
        if (temp != null) {
            //adding here;
            DatabaseReference addingItem = root.child(nameOfNextChild(numberOfChildren));
            addingItem.child("name").setValue(temp.getName());
            addingItem.child("alcohol").setValue(temp.getAlcohol());
            addingItem.child("country").setValue(temp.getCountry());
            addingItem.child("description").setValue(temp.getDescription());
            addingItem.child("price").setValue(temp.getPrice());
            addingItem.child("volume").setValue(temp.getVolume());
            addingItem.child("totalRate").setValue(0);
            addingItem.child("usersRate").child("temporaryrating").setValue(0);
            if (temp.getPrice2() != 0) {
                addingItem.child("price2").setValue(temp.getPrice2());
                addingItem.child("volume2").setValue(temp.getVolume2());
            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        numberOfChildren = dataSnapshot.getChildrenCount();
        addItemOnPossible(item);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
