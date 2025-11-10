@Validate_PC_Restricted_Tip_Message
Feature: Validate Profile Collections Restricted Tip Message in Job Mapping UI

  # This feature requires KF Super User role - all scenarios will be skipped for other roles

  @Navigate_to_UAM
  Scenario: Navigate to User Admin Module Dashboard
    Then Skip scenario if user is not KF Super User
    When User is on Profile Manager page
    Then Navigate to System Configuration Page form KFONE Global Menu
    Then Click on User Admin Module button
    Then User should be landed on Clients Dashboard page

  @NavigateToTeamsPage
  Scenario: Navigate to Teams page in User Admin Module
    Then Skip scenario if user is not KF Super User
    When User is on Clients Dashboard page
    Then Click on Teams section
    Then Verify User landed on Teams page

  @EnterTeamBasicInfo
  Scenario: Enter team name and description in Create Team page
    Then Skip scenario if user is not KF Super User
    When User is on Teams page
    Then Click on Create Teams button
    Then User should be navigated to first step of creating a team
    Then User should enter Team name and Team Description
    Then Click on Next button in Create Team page
    Then User should be navigated to second step of creating a team

  @AddTeamMembers
  Scenario: Add team members to the new team
    Then Skip scenario if user is not KF Super User
    When User is on second step of creating a team
    Then Search user to add as team member
    Then Select searched user to add as team member
    Then Click on Team Members header and Verify User is added successfully as team member

  @SaveAndVerifyTeamCreation
  Scenario: Save team and verify successful creation
    Then Skip scenario if user is not KF Super User
    When User is on Team creation page with team members added
    Then Click on Save button on Team page and verify success popup appears
    Then Search for Team name in Teams page and verify Team is created successfully

  @NavigateToProfileCollectionsPage
  Scenario: Navigate to Manage Success Profiles page in User Admin Module
    Then Skip scenario if user is not KF Super User
    Then Click on Profile Collections Section
    Then Verify User navigated to Manage Success Profiles page

  @EnterProfileCollectionBasicInfo
  Scenario: Enter Profile Collection name and description
    Then Skip scenario if user is not KF Super User
    When User is on Manage Success Profiles page
    Then Click on Create Profile Collection button
    Then User should navigated to first step of creating a Profile Collection
    Then User should enter Profile Collection Name and Profile Collection Description
    Then Click on Next button in Create Profile Collection page
    Then User should be navigated to second step of creating a Profile Collection

  @AssignTeamsToProfileCollection
  Scenario: Assign teams to the Profile Collection
    Then Skip scenario if user is not KF Super User
    When User is on second step of creating a Profile Collection
    Then Click on All Teams header
    Then Select Recently created team name
    Then Click on Selected Teams header
    Then User should verify Recently created team name is available in Selected Teams
    Then Click on Next button in Create Profile Collection page
    Then User should be navigated to third step of creating a Profile Collection

  @AddSuccessProfilesToCollection
  Scenario: Add Success Profiles to the Profile Collection
    Then Skip scenario if user is not KF Super User
    When User is on third step of creating a Profile Collection
    Then Click on Add Additional Success Profiles header
    Then Search and add Success Profiles to Profile Collection
    Then Click on Success Profiles header
    Then User should verify added Profiles are available in Success Profiles header

  @SaveAndVerifyProfileCollectionCreation
  Scenario: Save Profile Collection and verify successful creation
    Then Skip scenario if user is not KF Super User
    When User is on Profile Collection creation page with profiles added
    Then Click on Done button on Create Profile Collection page and verify success popup appears

 @Navigate_To_Job_Mapping
  Scenario: Navigate to Job Mapping page
  	Then Skip scenario if user is not KF Super User
    Then Navigate to Job Mapping page from KFONE Global Menu in PM
    Then User should be landed on Job Mapping page

  @Verify_PC_Restricted_Tip_Message
  Scenario: Verify PC Restricted Tip Message on Job Mapping page
    Then Skip scenario if user is not KF Super User
    Then Verify PC Restricted Tip Message is displaying on Job Mapping page

  @Navigate_to_UAM
  Scenario: Navigate to User Admin Module Dashboard
    Then Skip scenario if user is not KF Super User
    When User is on Profile Manager page
    Then Navigate to System Configuration Page form KFONE Global Menu
    Then Click on User Admin Module button
    Then User should be landed on Clients Dashboard page

  @NavigateToProfileCollectionsForCleanup
  Scenario: Navigate to Profile Collections page for cleanup
    Then Skip scenario if user is not KF Super User
    When User is on Clients Dashboard page
    Then Click on Profile Collections Section
    Then Verify User navigated to Manage Success Profiles page

  @DeleteProfileCollection
  Scenario: Delete Profile Collection
    Then Skip scenario if user is not KF Super User
    When User is on Manage Success Profiles page
    Then Search and delete Profile Collection

  @NavigateToTeamsPageForCleanup
  Scenario: Navigate to Teams page for cleanup
    Then Skip scenario if user is not KF Super User
    Then Click on Teams section
    Then Verify User landed on Teams page

  @DeleteTeam
  Scenario: Delete Team
    Then Skip scenario if user is not KF Super User
    When User is on Teams page
    Then Search for Team name in Teams page and delete team
