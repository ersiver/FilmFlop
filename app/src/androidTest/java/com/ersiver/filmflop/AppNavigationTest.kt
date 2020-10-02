package com.ersiver.filmflop

import android.view.Gravity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions.open
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ersiver.filmflop.di.RepoModule
import com.ersiver.filmflop.repository.MovieRepository
import com.ersiver.filmflop.util.EspressoIdlingResource
import com.ersiver.filmflop.utils.DataBindingIdlingResource
import com.ersiver.filmflop.utils.monitorActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@LargeTest
@UninstallModules(RepoModule::class)
@HiltAndroidTest
class AppNavigationTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: MovieRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }


    /**
     * Test to check that, if Drawer Menu item with id nav_settings
     * is clicked, then we navigate to the Settings screen.
     */
    @Test
    fun drawerNavigationFromHomeToSettings() {
        // start up Home screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //Check if the left drawer is closed on start and then open the drawer
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START)))
            .perform(open())

        //Navigate to Settings screen by clicking on the drawer menu item with Settings id
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_settings))

        // Check that Settings screen was opened.
        onView(withText("Switch Mode")).check(matches(isDisplayed()))

        // Check that when the  UpButton (<-) is clicked, then the Home screen is launched.
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.home_appbar)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    /**
     * Test to check that, if Drawer Menu item with id nav_about
     * is clicked, then we navigate to the About screen.
     */
    @Test
    fun drawerNavigationFromHomeToAbout() {
        // Start up Home screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //Check if the left drawer is closed on start and then open the drawer
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START)))
            .perform(open())

        //Navigate to About screen by clicking on the drawer menu item with About id
        onView(withId(R.id.nav_view)).perform(navigateTo(R.id.nav_about))

        // Check that About screen was opened.
        onView(withId(R.id.about_layout)).check(matches(isDisplayed()))

        //Check that when the UpButton (<-) is clicked, then the Home screen is launched.
        onView(withContentDescription("Navigate up")).perform(click())
        onView(withId(R.id.home_appbar)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    /**
     * Test to check that, if the HomeButton in the Home
     * screen is clicked, then the drawer is open.
     */
    @Test
    fun homeScreen_clickOnHomeButton_OpensDrawer() {
        // Start up Home screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //Check that the drawer is closed
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)))

        //Click on the home button
        onView(withContentDescription("Open drawer")).perform(click())

        //Verify the drawer is open
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)))

        activityScenario.close()
    }

    /**
     * Test to check that, if the "<-" btn in the Detail screen
     * is clicked, then we navigate back to the Home screen.
     */
    @Test
    fun movieDetailScreen__upButton_navigateToHomeScreen() {
        // Start up Home screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //Navigate to Detail screen by clicking on the movie on the list
        onView(withId(R.id.list)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )

        //Verify navigation to Detail.
        onView(withId(R.id.detail_toolbar)).check(matches(isDisplayed()))

        //click UpButton in the Detail screen
        onView(withContentDescription("Navigation up")).perform(click())

        //Verify navigation to back to Home screen.
        onView(withId(R.id.home_toolbar)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    /**
     * Test to check that, if the "<-" btn in the Search screen
     * is clicked, then we navigate back to the Home screen.
     */
    @Test
    fun searchScreen_upButton_navigateToHomeScreen() {
        // Start up Home screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Navigate to the Search screen
        activityScenario.onActivity {
            it.findNavController(R.id.nav_host_fragment).navigate(R.id.nav_search)
        }

        //Verify navigation to Detail.
        onView(withId(R.id.search_toolbar)).check(matches(isDisplayed()))

        //Click UpButton
        onView(withContentDescription("Navigation up")).perform(click())

        //Verify navigation to back to Home screen.
        onView(withId(R.id.home_toolbar)).check(matches(isDisplayed()))

        activityScenario.close()
    }
}