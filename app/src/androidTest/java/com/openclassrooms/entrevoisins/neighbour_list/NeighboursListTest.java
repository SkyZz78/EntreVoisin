
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import butterknife.OnClick;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;


    private ListNeighbourActivity mActivity;
    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    //private List<Neighbour> favorites = NeighbourApiService.getFavoriteNeighbours();
    //implementation des classes à utiliser

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);


    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mApiService = DI.getNewInstanceApiService();
        mNeighbours = mApiService.getNeighbours();
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        //Rajout allof à chaque ligne pour pouvoir retourner toute la liste des users sans quoi cela ne retournerait qu'une seule value
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours),isDisplayed())).check(withItemCount(ITEMS_COUNT-1));
    }

    @Test
    public void myNeighboursList_showProfileScreen() {
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(ViewMatchers.withId(R.id.neighbourPersonalPage)).check(matches(isDisplayed()));
    }

    @Test
    public void myNeighboursDetails_hasRightName() {
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(ViewMatchers.withId(R.id.neighbour_name)).check(matches(withText(mNeighbours.get(0).getName())));
    }


    @Test
    public void myNeighboursList_showFavorite() {
        //verifier que la liste de voisins favoris est vide
        onView(withContentDescription("Favorites")).perform(click());
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(0));
        //clic sur la vue neighbours et passage en favoris
        onView(withContentDescription("My neighbours")).perform(click());
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(ViewMatchers.withId(R.id.neighbour_fav_button)).perform(click());
        pressBack();
        //verifier si le voisin est en favoris
        onView(withContentDescription("Favorites")).perform(click());
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(1));
        //rentrer dans la vue et verifier que favoris est vide puis retour à liste initiale
        onView(allOf(ViewMatchers.withId(R.id.list_neighbours), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.neighbour_fav_button)).perform(click());
        pressBack();
        onView(withContentDescription("My neighbours")).perform(click());



    }



}