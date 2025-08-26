# Memory lane

Here is code that was once used in the project but has since been removed favouring improvements.
Code is kept here for reuse should it be needed as the app scales up.

---

## Add text notes button and animation

*MainActivity.java*

```java
private void setVisibility(boolean clicked) {
    if (!clicked) { //Since the button is not clicked by default, we negate the state when the button is clicked.
        addTextNoteFab.setVisibility(View.VISIBLE);
        addTextNoteFab.setClickable(true);
    } else {
        addTextNoteFab.setVisibility(View.INVISIBLE);
        addTextNoteFab.setClickable(false);
    }
}

private void onFabExpanded() {

    setVisibility(isAddNoteFabClicked);
    startAnimation(isAddNoteFabClicked);

    isAddNoteFabClicked = !isAddNoteFabClicked;
}

private void startAnimation(boolean clicked) {
    Animation fromBottom = Animations.FROM_BOTTOM.getAnimation(appContext);
    Animation toBottom = Animations.TO_BOTTOM.getAnimation(appContext);

    Animation rotateOpen = Animations.ROTATE_90_CLOCKWISE.getAnimation(appContext);
    Animation rotateClose = Animations.ROTATE_90_ANTI_CLOCKWISE.getAnimation(appContext);

    if (!clicked) {
        addTextNoteFab.startAnimation(fromBottom);
        addNoteFab.startAnimation(rotateOpen);
    } else {
        addTextNoteFab.startAnimation(toBottom);
        addNoteFab.startAnimation(rotateClose);
    }
}
```

*activity_main.xml*

```xml

<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:backgroundTint="@color/colourPrimary" android:clickable="false"
    android:contentDescription="@string/home_add_note_btn" android:focusable="false"
    android:id="@+id/home_add_text_note_fab" android:layout_height="wrap_content"
    android:layout_marginBottom="16dp" android:layout_width="wrap_content"
    android:visibility="invisible" app:fabCustomSize="70dp"
    app:layout_constraintBottom_toTopOf="@id/home_add_note_fab"
    app:layout_constraintEnd_toEndOf="@id/home_add_note_fab"
    app:layout_constraintStart_toStartOf="@id/home_add_note_fab"
    app:shapeAppearanceOverlay="@style/fab_rounded" app:srcCompat="@drawable/add_note_fab" />
```

---