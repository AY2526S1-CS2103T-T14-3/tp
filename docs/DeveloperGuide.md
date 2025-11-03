---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# FoodTrail Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org). <br>
* The [icon image](https://dribbble.com/shots/7057830-Food-Icon-Free-) for the application is credited to Phap Nguyen Huu.
* Declaration of the use of AI:

| Tool used | Person that used tool | Extent of Use                                                                                                                                                   |
|-----------|-----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Gemini    | Christopher Chong     | <li>Generation of test cases</li> <li>Troubleshooting to find the specific location of the problem</li>                                                         |
| Gemini    | Chen Junyao           | <li>Generation of test cases</li> <li>Troubleshooting to find the specific location of the problem</li> <li>Suggestions for improved phrasing of sentences</li> |
| Gemini    | Tan Weijun            | <li>Generation of background image for application</li> <li>Troubleshooting to find the specific location of the problem</li>                                   |
| Gemini    | Justin Chan           | <li>Generation of test cases</li> <li>Troubleshooting to find the specific location of the problem</li>                                                         |
| Gemini    | Louis Teng            | <li>Generation of test cases</li> <li>Troubleshooting to find the specific location of the problem</li>                                                         |

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the application.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/Main.java) and
[`MainApp`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the application.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the application in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside components being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `RestaurantListPanel`,
`StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Restaurant` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `RestaurantDirectoryParser` object which in
   turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a restaurant).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `RestaurantDirectoryParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `RestaurantDirectoryParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the restaurant directory data i.e., all `Restaurant` objects (which are contained in a `UniqueRestaurantList` object).
* stores the currently 'selected' `Restaurant` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Restaurant>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `RestaurantDirectory`, 
which `Restaurant` references. This allows `RestaurantDirectory` to only require one `Tag` object per unique tag, instead of each `Restaurant` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both restaurant directory data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `RestaurantDirectoryStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `foodtrail.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

### Planned Enhancements

**Team size: 5**

1. **Allow find command to filter by specific field**: The current find command searches across `Name`, `Phone`, 
   `Address`, and `Tag` simultaneously, which does not give users an explicit way to search by a specific field. 
    We plan to allow specifying a prefix so user can specify the field to search by.
   * `find n/kfc` would search through `Name` in the restaurant directory for the keyword `kfc`.
   * `find hp/283` would search through `Phone` in the restaurant directory for the keyword `283`.   
2. **Allow find command to search through ratings**: The current find command searches across `Name`, `Phone`, `Address`,
   and `Tag`, which does not allow users to search by their ratings. We plan to include support for ratings so users 
   could
   search for restaurants based on specific ratings.
   * `find r/4` would find all restaurants that the user has rated 4 stars.
3. **Allow edit command to edit tags**: The current edit command only allows editing of `Name`, `Phone`,
   `Address`, without being able to edit tags. We plan to include support for tags so that users do not need to 
   resort to `untag` and `tag` to edit tags.
    * `edit 1 ot/old_tag nt/new_tag` would edit the `old_tag` of the 1st restaurant in the current directory with `new_tag`.
4. **Allow tag and untag command to accept multiple indexes**: The current tag and untag commands only allows modifying 
   the tags of one restaurant at a time.
   We plan to allow listing more indexes so that multiple restaurants can be tagged/untagged at once.
   * `tag 1 2 3 t/fast food` would add a `fast food` tag for the 1st, 2nd, and 3rd restaurant in the current directory.
   * `untag 4 5 t/chicken` would remove the `chicken` tag for the 4th and 5th restaurant in the current directory.
5. **Make Address error message more specific**: The current `Address` error message lists multiple 
   constraints, which is too generic since it does not pinpoint the specific constraint that was violated. We 
   plan to split the constraints into their own error messages and display only the violated constraints.
6. **Rename clear command to make it harder to enter by accident**: The current clear command deletes all restaurant data once entered, and users could potentially enter the `clear` command when trying to delete a restaurant. 
   We plan to rename the command to `clearalldata` so that it is harder to erase all data by accident.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* food enthusiasts who want to keep track of restaurants in Singapore that they have visited or are interested in
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:

* maintain a curated record of dining experiences
* eliminates the need for scattered notes or relying on memory
* helps users keep track of visited restaurants


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​     | I want to …​                          | So that I can…​                                                       |
|----------|-------------|---------------------------------------|-----------------------------------------------------------------------|
| `* * *`  | user        | list all restaurants                  | see all the places that I have been to                                |
| `* * *`  | user        | add a new restaurant                  | document my experience at a place that I have been to                 |
| `* * *`  | user        | remove an existing restaurant         | remove a place that I do not want to visit                            |
| `* *`    | user        | save added restaurants to a file      | access past data in different sessions                                |
| `* *`    | user        | mark an address as visited            | differentiate between places that I have visited or have yet to visit |
| `* *`    | user        | tag a restaurant                      | remember what food was memorable at that place                        |
| `* *`    | user        | add a picture to a restaurant         | remember what the food looked like                                    |
| `* *`    | expert user | edit the address of a restaurant      | update the new location of the restaurant after it has moved.         |
| `* *`    | user        | rate the food quality at a restaurant | remember my experience at that place                                  |
| `* *`    | user        | view the user guide easily            | learn more about the product as and when I need                       |
| `* *`    | user        | filter restaurants                    | find all restaurants that has a specific food                         |
| `*`      | user        | find restaurants that are near me     | see if there are any places to eat around me                          |

### Use cases

(For all use cases below, the **System** is the `FoodTrail` and the **Actor** is the `User`, unless specified otherwise)

**Use case: UC01 - Add a restaurant**

MSS:

1. User enters details of the restaurant to be added.
2. FoodTrail adds the restaurant to the list.

   Use case ends.

Extensions:

* 1a. The input does not follow the command format.

    * 1a1. FoodTrail shows an error message, notifying the user about the command format.

      Use case resumes at step 1.

* 1b. The entered restaurant details matches an existing restaurant in the restaurant directory.

    * 1b1. FoodTrail shows an error message, notifying the user that the restaurant already exists in the restaurant directory.

      Use case resumes at step 1.

<br>

**Use case: UC02 - List restaurants**

MSS:

1. User requests to list restaurants.
2. FoodTrail shows a list of restaurants.

   Use case ends.

Extensions:

* 1a. The list is empty.

  Use case ends.

<br>

**Use case: UC03 - Find a restaurant**

MSS:

1. User requests to find restaurants matching a given keyword.
2. FoodTrail displays a filtered directory of restaurants whose name, phone number, address, or tag matches the keyword.

   Use case ends.

Extensions:

* 1a. The input does not follow the command format.

    * 1a1. FoodTrail shows an error message, notifying the user about command format for find.

      Use case resumes at step 1.

* 1b. No restaurants match the given keyword.

    * 1b1. FoodTrail displays 0 restaurants in the current directory.

      Use case ends.

<br>

**Use case: UC04 - Edit a restaurant**

MSS:
1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to edit the details of a specific restaurant in the current directory.
3. FoodTrail updates the restaurant's details with the provided information.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for edit.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

* 2c. No prefix was provided.

    * 2c1. FoodTrail shows an error message, notifying the user that one field must be provided.

      Use case resumes at step 2.

* 2d. The edited details reflect an existing restaurant in the current directory.

    * 2d1. FoodTrail shows an error message, notifying the user about the duplicate restaurant.

      Use case resumes at step 2.

<br>

**Use case: UC05 - Delete a restaurant**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to delete a specific restaurant in the current directory.
3. FoodTrail deletes the specified restaurant.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for delete.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

<br>

**Use case: UC06 - Mark a restaurant as visited**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to mark a specific restaurant in the current directory as visited.
3. FoodTrail marks the specified restaurant as visited.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for mark.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

* 2c. The restaurant of the specified index is already marked as visited.

    * 2c1. FoodTrail shows an error message, notifying the user that the restaurant is already marked as visited.

      Use case resumes at step 2.

<br>

**Use case: UC07 - Unmark a restaurant as visited**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to unmark a specific restaurant in the current directory as visited.
3. FoodTrail unmarks the specified restaurant as visited.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for unmark.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

* 2c. The restaurant of the specified index is already unmarked.

    * 2c1. FoodTrail shows an error message, notifying the user that the restaurant is already unmarked.

      Use case resumes at step 2.

<br>

**Use case: UC08 - Rate a restaurant**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to rate a specific restaurant in the current directory.
3. FoodTrail adds a rating for the specified restaurant.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for rate.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

* 2c. The specified restaurant already has the specified rating.

    * 2c1. FoodTrail shows an error message, notifying the user that the restaurant already has the same rating.

      Use case resumes at step 2.

<br>

**Use case: UC09 - Unrate a restaurant**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to remove a rating from a specific restaurant in the current directory.
3. FoodTrail removes the rating from the specified restaurant.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for unrate.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

* 2c. The specified restaurant does not have a rating.

    * 2c1. FoodTrail shows an error message, notifying the user that the restaurant does not have a rating.

      Use case resumes at step 2.

<br>

**Use case: UC10 - Tag a restaurant**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to add a tag to a specific restaurant in the current directory.
3. FoodTrail tags the restaurant with the provided tag.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for tag.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

* 2c. The specified restaurant already has the provided tag.

    * 2c1. FoodTrail shows an error message, notifying the user that the specified restaurant already has the provided tag.

      Use case resumes at step 2.

<br>

**Use case: UC11 - Untag a restaurant**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC02)</span>.
2. User requests to remove a tag from a specific restaurant in the current directory.
3. FoodTrail untags the restaurant with the provided tag.

   Use case ends.

Extensions:

* 2a. The input does not follow the command format.

    * 2a1. FoodTrail shows an error message, notifying the user about command format for untag.

      Use case resumes at step 2.

* 2b. The given index is invalid.

    * 2b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

* 2c. The specified restaurant does not have the provided tag.

    * 2c1. FoodTrail shows an error message, notifying the user that the specified restaurant does not have the provided tag.

      Use case resumes at step 2.

<br>

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 100 restaurants without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Response to any user action should be within 3 seconds.
5. User interface should have clear prompts and informative error messages.
6. Source code should be open-source.
7. Code should be well-commented, and a user guide should be provided.
8. Features should be implemented in separate modules for easier debugging and updates.
9. Error messages should be descriptive when reporting failure.
10. Program should not crash upon encountering errors.

<br>

### Glossary

* **API**: The public set of methods a component exposes, independent of its implementation
* **Architecture Diagram**: High-level overview of components and their relationships
* **CLI**: Refers to Command Line Interface, a text-based method for users to interact with the program
* **FXML**: XML files describing JavaFX UI layouts
* **GUI**: Refers to Graphical User Interface, a user-friendly visual mechanism that allows users to interact with the system
* **JAR**: Packaged executable of the app
* **JavaFX**: GUI framework used to build the app’s interface
* **JSON**: Text format used to serialize FoodTrail data and user preferences
* **Mainstream OS**: Windows, Linux, Unix, macOS
* **MSS (Main Success Scenario)**: The normal, no-errors path in a use case

--------------------------------------------------------------------------------------------------------------------
## **Appendix: Instructions for manual testing**

Given below are instructions to test the application manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch
   1. [Download](https://github.com/AY2526S1-CS2103T-T14-3/tp/releases/download/v1.6/foodtrail.jar) the jar file and copy into a folder.
   2. Open a command terminal and `cd` into the folder where the jar file is downloaded.
   3. Launch the jar file using `java -jar foodtrail.jar`. <br>
      Expected: Shows the GUI with a set of sample restaurants. The window size may not be optimum.
      
<br>

2. Saving window preferences
   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   2. Re-launch the application by entering `java -jar foodtrail.jar` in the command terminal.<br>
      Expected: The most recent window size and location is retained.

<br>

### Listing all restaurants: list

1. Listing all restaurants in the restaurant directory

   * Prerequisites: None.

   1. Test case: `list`<br>
      Expected: All restaurants in the restaurant directory are shown.

   2. Test case: `list x`<br>
      Expected: Similar to previous.

<br>

### Adding a restaurant: add

1. Adding a restaurant to the restaurant directory
    * Prerequisites: There is no restaurant named `McDonald's` in the restaurant directory with the phone number
      `68928572` and the address `1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743`. 
      
    1. Test case: `add n/McDonald's hp/68928572 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 t/halal t/fast food` <br>
       Expected: A new restaurant called `McDonald's` is added to the directory. The output box shows the details of 
       the added restaurant as well.<br>

2. Adding a duplicate restaurant
    * Prerequisites: There is an existing restaurant named `McDonald's` in the restaurant directory with the phone number
      `68928572` and the address `1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743`. 
      
    1. Test case: `add n/McDonald's hp/68928572 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 t/halal t/fast food` <br> 
       Expected: The restaurant is not added. The output box will display an error message indicating the restaurant already exists in the restaurant directory.<br>

3. Providing an invalid input
    * Prerequisites: None.

    1. Test case: `add n/ hp/68928572 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 t/halal t/fast food` (invalid name) <br>
       Expected: The restaurant is not added. The output box will display an error message indicating an invalid name is provided.
   
    2. Test case: `add n/McDonald's hp/68928572 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, singapore 77743 t/halal t/fast food` (invalid address) <br>
       Expected: The restaurant is not added. The output box will display an error message indicating an invalid address is provided.
   
    3. Test case: `add n/McDonald's hp/58928572 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 t/halal t/fast food` (invalid phone number) <br>
       Expected: The restaurant is not added. The output box will display an error message indicating an invalid phone number is provided.<br>

4. Missing a prefix or parameter
    * Prerequisites: None.

    1. Test case: `add KFC hp/68928572 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 t/halal t/fast food` (missing `n/` prefix) <br>
       Expected: The restaurant is not added. The output box will display an error message indicating an invalid command and the syntax for the add command.

    2. Other incorrect commands to try: missing `a/` prefix, missing `hp/` prefix <br>
       Expected: Similar to previous.

    3. Test case: `add hp/68928572 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743 t/halal t/fast food` (missing name) <br>
       Expected: The restaurant is not added. The output box will display an error message indicating an invalid command and the syntax for the add command.

    4. Other incorrect commands to try: missing address, missing phone number <br>
       Expected: Similar to previous.

<br>

### Deleting a restaurant: delete

1. Deleting a restaurant in the restaurant directory
   * Prerequisites: There must be at least one restaurant in the current restaurant directory.

   1. Test case: `delete 1`<br>
      Expected: The 1st restaurant is deleted from the restaurant directory. Details of the deleted restaurant is shown in the output box.<br>

2. Missing index
    * Prerequisites: None.

    1. Test case: `delete`<br>
       Expected: No restaurant is deleted. The output box will display an error message indicating an invalid command and the syntax for the delete command.<br>

3. Providing an invalid index
   * Prerequisites: There is less than 100 restaurants in the current directory.

   1. Test case: `delete 100`<br>
      Expected: No restaurant is deleted. The output box will display an error message indicating an invalid index is provided.

   2. Test case: `delete x`<br>
      Expected: No restaurant is deleted. The output box will display an error message indicating an invalid command and the syntax for the delete command.

<br>

### Editing a restaurant: edit

1. Editing the details of an existing restaurant in the restaurant directory
    * Prerequisites: There must be at least one restaurant in the current restaurant directory. There is no restaurant named `McDonald's` in the restaurant directory with the phone number
     `91234567` and the address `1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743`.

    1. Test case: `edit 1 hp/91234567` <br>
       Expected: The phone number of the 1st restaurant is updated to `91234567`. The output box shows the corresponding 
       details of the updated restaurant.
   
    2. Test case: `edit 1 n/McDonald's` <br>
       Expected: The name of the 1st restaurant is updated to `McDonald's`. The output box shows the corresponding details of 
       the updated restaurant.
   
    3. Test case: `edit 1 a/1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743` <br>
       Expected: The address of the 1st restaurant is updated to `1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743`. The output box shows the corresponding 
       details of the updated restaurant.

    4. Test case: `edit 1 n/McDonald's hp/91234567` <br>
       Expected: The name of the 1st restaurant is updated to `KFC` and the phone number is updated to `91234567`. 
       The output box shows the corresponding details of the updated restaurant.<br>

2. Editing a restaurant to become another duplicate restaurant
    * Prerequisites: There must be at least two restaurants in the current restaurant directory. The 1st restaurant is named `McDonald's` in the restaurant directory with the phone number
      `91234567` and the address `1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743`. The 2nd restaurant is named `KFC` in the restaurant directory with the phone number
      `91234567` and the address `1 Jelebu Road, #02-01, Bukit Panjang Plaza, Singapore 677743`.
   
    1. Test case: `edit 2 n/McDonald's` <br>
       Expected: The 2nd restaurant is not edited. The output box will display an error message indicating the restaurant already exists in the restaurant directory.<br>

3. Missing prefix 
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `edit 1 61234567`<br>
       Expected: The 1st restaurant is not edited. The output box will display an error message indicating an invalid command and the syntax for the edit command.<br>

4. Missing value
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `edit 1 hp/`<br>
       Expected: The 1st restaurant is not edited. The output box will display an error message indicating an invalid command and the syntax for the edit command.<br>

5. Missing prefix and value
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `edit 1`<br>
       Expected: The 1st restaurant is not edited. The output box will display an error message informing the user to edit at least one field.<br>

6. Missing index
    * Prerequisites: None.

    1. Test case: `edit n/KFC`<br>
       Expected: No restaurant is edited. The output box will display an error message indicating an invalid command and the syntax for the edit command.<br>

7. Providing an invalid index
    * Prerequisites: There is less than 100 restaurants in the current directory.

    1. Test case: `edit 100 n/KFC`<br>
       Expected: No restaurant is edited. The output box will display an error message indicating an invalid index is provided.

<br>

### Adding a tag to a restaurant: tag

1. Adding tags for an existing restaurant in the restaurant directory
    * Prerequisites: There must be at least one restaurant in the current directory. The 1st restaurant does not 
     have any tags.
   
    1. Test case: `tag 1 t/halal` <br>
       Expected: A `halal` tag is added to the 1st restaurant. The output box shows the corresponding details of 
       the updated restaurant.
   
    2. Test case: `tag 1 t/halal t/fast food` <br>
       Expected: A `halal` and `fast food` tag is added to the 1st restaurant. The output box shows the corresponding details of
       the updated restaurant.<br>

2. Adding a duplicate tag 
    * Prerequisites: There must be at least one restaurant in the current directory. The 1st restaurant has a `halal` 
      tag.

    1. Test case: `tag 1 t/halal` <br>
       Expected: The `halal` tag is not added again to the 1st restaurant. The output box will display an error message indicating that the `halal` tag already exists for the 1st restaurant.<br>

3. Missing prefix
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `tag 1 halal`<br>
       Expected: No tag is added to the 1st restaurant. The output box will display an error message indicating an invalid command and the syntax for the tag command.<br>

4. Missing value
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `tag 1 t/` <br>
       Expected: No tag is added to the 1st restaurant. The output box will display an error message indicating that the tag name cannot be empty.<br>

5. Missing prefix and value
    * Prerequisites: There must be at least one restaurant in the current directory.

       1. Test case: `tag 1` <br>
          Expected: No tag is added to the 1st restaurant. The output box will display an error message indicating an invalid command and the syntax for the tag command.<br>

6. Missing index
    * Prerequisites: None.

    1. Test case: `tag t/halal`<br>
       Expected: No tag is added. The output box will display an error message indicating an invalid command and the syntax for the tag command.<br>

7. Providing an invalid index
    * Prerequisites: There is less than 100 restaurants in the current directory.

    1. Test case: `tag 100 t/halal`<br>
       Expected: No tag is added. The output box will display an error message indicating an invalid index is provided.

    2. Test case: `tag x t/halal`<br>
       Expected: No tag is added. The output box will display an error message indicating an invalid command and the syntax for the tag command.

<br>

### Removing a tag from a restaurant: untag

1. Removing a tag for an existing restaurant in the restaurant directory
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `untag 1 t/halal` <br>
       Expected: The `halal` tag of the 1st restaurant is removed. The output box shows the corresponding details of the updated restaurant.<br>

2. Removing a non-existent tag for an existing restaurant in the restaurant directory
    * Prerequisites: There must be at least one restaurant in the current directory. The 1st restaurant has a `halal` tag.

    1. Test case: `tag 1 t/fast food` <br>
       Expected: No tag is removed from the 1st restaurant. The output box will display an error message indicating that the `fast food` tag does not exist for the 1st restaurant.<br>

3. Missing prefix
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `untag 1 halal`<br>
       Expected: No tag is removed from the 1st restaurant. The output box will display an error message indicating an invalid command and the syntax for the untag command.<br>

4. Missing value
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `untag 1 t/` <br>
       Expected: No tag is removed from the 1st restaurant. The output box will display an error message indicating that the tag name cannot be empty.<br>

5. Missing prefix and value
    * Prerequisites: There must be at least one restaurant in the current directory.

        1. Test case: `untag 1` <br>
           Expected: No tag is removed from the 1st restaurant. The output box will display an error message indicating an invalid command and the syntax for the untag command.<br>

6. Missing index
    * Prerequisites: None.

    1. Test case: `untag`<br>
       Expected: No tag is removed. The output box will display an error message indicating an invalid command and the syntax for the untag command.<br>

7. Providing an invalid index
    * Prerequisites: There is less than 100 restaurants in the current directory.

    1. Test case: `untag 100`<br>
       Expected: No tag is removed. The output box will display an error message indicating an invalid index is provided.

    2. Test case: `untag x`<br>
       Expected: No tag is removed. The output box will display an error message indicating an invalid command and the syntax for the untag command.

<br>

### Finding restaurants: find

1. Finding restaurants by a certain keyword
    * Prerequisites: None.

    1. Test case: `find aston` <br>
       Expected: The current directory is filtered to show only restaurants whose name, phone number, address, or tag contains `aston`.

    2. Test case: `find fast food` <br>
       Expected: The current directory is filtered to show only restaurants whose name, phone number, address, or tag contains `fast food`.<br>

2. Missing keyword
    * Prerequisites: None.

    1. Test case: `find` <br>
       Expected: The current directory remains unchanged. The output box will display an error message indicating an invalid command and the syntax for the find command.

<br>

### Marking a restaurant as visited: mark

1. Marking a restaurant as visited
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `mark 1` <br>
       Expected: The 1st restaurant is marked as visited. The output box shows the corresponding details of the marked restaurant.<br>

2. Missing index
    * Prerequisites: None.

    1. Test case: `mark`<br>
       Expected: No restaurant is marked as visited. The output box will display an error message indicating an invalid command and the syntax for the mark command.<br>

3. Providing an invalid index
    * Prerequisites: There is less than 100 restaurants in the current directory.

    1. Test case: `mark 100`<br>
       Expected: No restaurant is marked as visited. The output box will display an error message indicating an invalid index is provided.

    2. Test case: `mark x`<br>
       Expected: No restaurant is marked as visited. The output box will display an error message indicating an invalid command and the syntax for the mark command.

<br>

### Marking a restaurant as not visited: unmark

1. Marking a restaurant in the restaurant directory as not visited 
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `unmark 1` <br>
       Expected: The 1st restaurant is unmarked. The output box shows the corresponding details of the unmarked restaurant.<br>

2. Missing index
    * Prerequisites: None.

    1. Test case: `unmark`<br>
       Expected: No restaurant is unmarked. The output box will display an error message indicating an invalid command and the syntax for the unmark command.<br>

3. Providing an invalid index
    * Prerequisites: There is less than 100 restaurants in the current directory.

    1. Test case: `unmark 100`<br>
       Expected: No restaurant is unmarked. The output box will display an error message indicating an invalid index is provided.

    2. Test case: `unmark x`<br>
       Expected: No restaurant is unmarked. The output box will display an error message indicating an invalid command and the syntax for the unmark command.

<br>

### Adding a rating for a restaurant: rate

1. Adding a rating for an existing restaurant in the restaurant directory
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `rate 1 r/5` <br>
       Expected: The rating of the 1st restaurant is updated to 5. The output box will display the restaurant name and the added rating.
   
    2. Test case: `rate 1 r/0` <br>
       Expected: The rating of the 1st restaurant is updated to 0. The output box will display the restaurant name and the added rating.<br>

2. Adding an invalid rating
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `rate 1 r/6` <br>
       Expected: No rating is added to the 1st restaurant. The output box will display an error message informing the user to enter an integer from 0 to 5.<br>

3. Adding the same rating
    * Prerequisites: There must be at least one restaurant in the current directory. The 1st restaurant has a rating of 5.

    1. Test case: `rate 1 r/5` <br>
       Expected: The rating remains the same for the 1st restaurant. The output box will display an error message indicating the restaurant already has the same rating. <br>

4. Missing prefix
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `rate 1 5` <br>
       Expected: No rating is added to the 1st restaurant. The output box will display an error message informing the user to provide the `r/` prefix.<br>

5. Missing value
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `rate 1 r/` <br>
       Expected: No rating is added to the 1st restaurant. The output box will display an error message informing the user to enter an integer from 0 to 5.<br>

6. Missing prefix and value
    * Prerequisites: There must be at least one restaurant in the current directory.

    1. Test case: `rate 1` <br>
       Expected: No rating is added to the 1st restaurant. The output box will display an error message indicating an invalid command and the syntax for the rate command.<br>

7. Missing index
    * Prerequisites: None.

    1. Test case: `rate r/5`<br>
       Expected: No rating is added. The output box will display an error message indicating an invalid command and the syntax for the rate command.<br>

8. Providing an invalid index
    * Prerequisites: There is less than 100 restaurants in the current directory.

    1. Test case: `rate 100 r/5`<br>
       Expected: No rating is added. The output box will display an error message indicating an invalid index is provided.

    2. Test case: `rate x r/5`<br>
       Expected: No rating is added. The output box will display an error message indicating that the index is not a non-zero unsigned integer.

<br>

### Removing a rating from a restaurant: unrate

1. Removing a rating from a restaurant in the restaurant directory
    * Prerequisites: There must be at least one restaurant in the current directory. The 1st restaurant has a rating.

    1. Test case: `unrate 1` <br>
       Expected: The rating of the 1st restaurant is removed. The output box will display a message that the rating is removed from the restaurant.<br>

2. Removing a rating for a restaurant without a rating 
    * Prerequisites: There must be at least one restaurant in the current directory. The 1st restaurant does not have a rating.

    1. Test case: `unrate 1` <br>
       Expected: No rating is removed from the 1st restaurant. The output box will display an error message indicating the restaurant has no rating to remove.<br>

3. Missing index
    * Prerequisites: None.

    1. Test case: `unrate`<br>
       Expected: No rating is removed. The output box will display an error message indicating an invalid command and the syntax for the unrate command.<br>

4. Providing an invalid index
    * Prerequisites: There is less than 100 restaurants in the current directory.

    1. Test case: `unrate 100`<br>
       Expected: No rating is removed. The output box will display an error message indicating an invalid index is provided.

    2. Test case: `unrate x`<br>
       Expected: No rating is removed. The output box will display an error message indicating that the index is not a non-zero unsigned integer.
