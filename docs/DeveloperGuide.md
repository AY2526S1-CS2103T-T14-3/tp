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

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/Main.java) and 
[`MainApp`](https://github.com/AY2526S1-CS2103T-T14-3/tp/blob/master/src/main/java/foodtrail/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
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

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* food enthusiasts who want to keep track of restaurants they have visited or are interested in
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps
* food lovers that are young adults ranging from 21 to early 30s

**Value proposition**:

* maintain a curated record of dining experiences
* eliminates the need for scattered notes or relying on memory
* helps users keep track of favourite restaurants


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

(For all use cases below, the **System** is the `FoodTrail` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - List restaurants**

MSS:

1. User requests to list restaurants.
2. FoodTrail shows a list of restaurants.

    Use case ends.

Extensions:

* 2a. The list is empty.

  Use case ends.


**Use case: UC02 - Add a restaurant**

MSS:

1. User enters details of the restaurant to be added.
2. FoodTrail adds the restaurant to the list.

   Use case ends.

Extensions:

* 2a. There are missing parameters or invalid syntax.

    * 2a1.  FoodTrail shows an error message, notifying the user about the syntax for add.

      Use case resumes at step 1.


**Use case: UC03 - Delete a restaurant**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC01)</span>.
2. User requests to delete a specific restaurant in the list.
3. FoodTrail deletes the specified restaurant.

    Use case ends.

Extensions:

* 3a. The given index is invalid.

    * 3a1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.


**Use case: UC04 - Mark a restaurant as visited**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC01)</span>.
2. User requests to mark a specific restaurant in the list as visited.
3. FoodTrail marks the specified restaurant as visited.

   Use case ends.

Extensions:

* 3a. The given index is invalid.

    * 3a1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.


**Use case: UC05 - Tag a restaurant**

MSS:

1. User <span style="text-decoration:underline">lists restaurants (UC01)</span>.
2. User requests to tag a specified restaurant in the list with a specified tag.
3. FoodTrail tags the restaurant with the provided tag.

   Use case ends.

Extensions:

* 3a. There is missing index or tag.

    * 3a1. FoodTrail shows an error message, instructing the user to provide an index and tag.

    Use case resumes at step 2.


* 3b. The given index is invalid.

    * 3b1. FoodTrail shows an error message, notifying the user about the invalid index.

      Use case resumes at step 2.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 100 restaurants without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Response to any user action should be within 3 seconds.
5. User interface should have clear prompts and informative error messages.
6. Source code should be open-source.
7. Code should be well-commented, and a user guide should be provided.
8. Features should be implemented in separate modules for easier debugging and updates.
9. Error messages should suggest corrective action, not just report failure.
10. Program should not crash upon encountering errors.

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

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample restaurants. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.


### Deleting a restaurant

1. Deleting a restaurant while all restaurants are being shown

   1. Prerequisites: List all restaurant using the `list` command. Multiple restaurants in the list.

   1. Test case: `delete 1`<br>
      Expected: First restaurant is deleted from the list. Details of the deleted restaurant shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No restaurant is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.
