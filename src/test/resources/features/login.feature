@login
Feature: Login
  Log in to Matchday
  
  Scenario: Successful loging in to Matchday
    Given I am on Matchday login page
    When I fill username field with
     | user |
     | admin |
    And I fill password field with
     | pass |
     | boss |
    Then I am logged in to Matchday 
    
  Scenario: Unsuccessful loging in to Matchday
    Given I am on Matchday login page
     When I fill username field with
     | edmund |
     | andy |
    And I fill password field with
     | psswrd |
     | 123frytki |
    Then I am not logged in to Matchday 