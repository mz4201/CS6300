1. When the app is started, the user is presented with the main menu, which allows the user to (1) enter or edit current job details, (2) enter job offers, (3) adjust the comparison settings, or (4) compare job offers (disabled if no job offers were entered yet).  
 > This is implemented with a Main Menu Class containing four methods: currentJob(), jobOffer(), comparisonSetting(), and compareJobOffers().  
2. When choosing to enter current job details, a user will:
   + Be shown a user interface to enter (if it is the first time) or edit all the details of their current job, which consists of: 
     - Title
     - Company
     - Location (entered as city and state)
     - Cost of living in the location (expressed as an index)
     - Yearly salary
     - Yearly bonus
     - Training and Development Fund
     - Leave Time
     - Telework Days per Week
   + Be able to either save the job details or cancel and exit without saving, returning in both cases to the main menu.
> This is implemented with currentJob class and Job class to distinguish entering current job details. Job class included attributes to allow for cancel, save, and return to main menu. The class will also store job details listed above and cannot be shared with other class.
3. When choosing to enter job offers, a user will:
   + Be shown a user interface to enter all the details of the offer, which are the same ones listed above for the current job.
   + Be able to either save the job offer details or cancel.
   + Be able to (1) enter another offer, (2) return to the main menu, or (3) compare the offer (if they saved it) with the current job details (if present).
> This is implemented with jobOffer class and Job class to distinguish entering a job offer. Job class contains attribute to allow save, cancel, enter another offer, return to main menu and compare with currentJob by calling CompareJobs class. It collects the above listed information and does not share with other class. 
4. When adjusting the comparison settings, the user can assign integer weights to:
   + Yearly salary
   + Yearly bonus
   + Training and Development Fund
   + Leave Time
   + Telework Days per Week
   
   If no weights are assigned, all factors are considered equal.
   
    NOTE: These factors should be integer-based from 0 (no interest/don’t care) to 9 (highest interest)
> This is implemented by comparisonSetting class collecting the above list of details from user. The data cannot be shared with otherclass. The scenario of no weights assigned would be defined in the code, no need to be included in the design. Datatype set the rule to only allow integer entry.
5. When choosing to compare job offers, a user will:
   + Be shown a list of job offers, displayed as Title and Company, ranked from best to worst (see below for details), and including the current job (if present), clearly indicated.
   + Select two jobs to compare and trigger the comparison.
   + Be shown a table comparing the two jobs, displaying, for each job:
     - Title
     - Company
     - Location
     - Yearly salary adjusted for cost of living
     - Yearly bonus adjusted for cost of living
     - TDF = Training and Development Fund
     - LT = Leave Time
     - RWT = Telework Days per Week
   + Be offered to perform another comparison or go back to the main menu.
> This is implemented by the CompareJobs class, which will get job detail from Job class, then use RankJob class and ComparisonSetting class to perform the function of comparing jobs. It will have methods to rank job, display the list of job offers, compare jobs (can display results listed above), and return to main menu. The action of selected jobs will store the jobs in a private list. The implementation of comparing another offer is by using the reset() method to allow to reselect jobs.
6. When ranking jobs, a job’s score is computed as the weighted average of:

    AYS + AYB + TDF + (LT * AYS / 260) - ((260 - 52 * RWT) * (AYS / 260) / 8))

    where:

    AYS = Yearly Salary Adjusted for cost of living
    
    AYB = Yearly Bonus Adjusted for cost of living
    
    TDF = Training and Development Fund  ($0 to $18000 inclusive annually)
    
    LT = Leave Time  (0-100 whole number days inclusive)
    
    RWT = Telework Days per Week

    NOTE: The rationale for the RWT subformula is:
    + value of an employee hour = (AYS / 260) / 8
    + commute hours per year (assuming a 1-hour/day commute): 1 * (260 - 52 * RWT)
    + therefore travel-time cost = (260 - 52 * RWT) * (AYS / 260) / 8

    For example, if the weights are 2 for the yearly salary, 2 for the yearly bonus, 2 for the Training and Development Fund, and 1 for all other factors, the score would be computed as:

    2/8 * AYS + 2/8 * AYB + 2/8 * TDF + 1/8 * (LT * AYS / 260) - 1/8 * ((260 - 52 * RWT) * (AYS / 260) / 8)))
> This is implemented by rankJob class that is called from compareJob class and utilize comparisonSetting class. It includes a method that will use the formula and data from comparisonSetting class to calculate the job score for ranking.
7. The user interface must be intuitive and responsive.
> This aspect is not reflected in my design because it will be managed entirely within the GUI implementation.
8. For simplicity, you may assume there is a single system running the app (no communication or saving between devices is necessary).
> This is shown by not having any methods to consider save data between multiple devices.