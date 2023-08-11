# erp
My First Project
- In 2019 as a R&D manager of my company, whose name was Daesan Vina at that time, I was assigned the job to monitor sales
  between my company and a customer that focused on exporting garments. We were supposed to sell dyed fabric to them,
- Our normal sales to the local market does not need any standard vouchers, everything was recorded by hands into a physical ledger by the boss,
- Our main product was un-dyed, greige fabric,
- Working with an exporting customer demanded much update to our voucher system, whether it was the delivery notice, or item identification system,
  or even account-settling form. We did not even have a mature inventory monitoring system back then. Everthing was done by hand and on-demand,
  which was not even up to par with a normal periodic inventory system.
- My journey started from using MS Excel (2019) to MS Access (2020-2022) to Spring Boot App (2023).
- Now in July 2023, the simple erp-application was uploaded to Git, but not quite complete yet.

So, let's summary the content of this application:
- First is the heart of the application: the relational database, ie. the Entities. It can be separated into 9 clusters:
   1. Company cluster: Company - Address - Contact data (eg. Daesan Company - HQ Address - My Contact)
   2. Item Code cluster: ItemCategory - ItemType - ItemCode - ItemCodeSupplier - Equivalent 
      (eg. Fabric - 70D Interlock - BLACK DS19031444-A - JETBLACK 01A (linked with Company NamThanh) - Eqv 1kg -> 6.76 yards)
   3. Purchase cluster: not yet done, supposed to save Purchase orders from each department of Daesan Vina.
   4. Inventory cluster: InventoryIn - Inventory - InventoryOut
      (eg. 220425 goods-purchase/manufactured/gift-received voucher -
        Inventories in voucher with init qtt. and remaining qtt. -
        selling/gifting/taking as input of manufaturing parts of those Inventories)
   5. Sales cluster: Sale - SaleArticle - SaleContainer - SaleLot
      (Order name TU/FABRIC/1569i -
        Buy Article specifies itemCode BLACK DS19031444-A -
        Container by unit of YARD -
        Lot 1 delegates to NT Company; Lot 2 delegates to Self-manufacturing
        Linking each SaleLot with corresponding InventoryOut
   6. Manufacturing cluster: not yet done
   7. Giftout cluster: not yet done
   8. Warehouse transferring cluster: not yet done
   9. User cluster: User - Role: saving users roles and log in information to get permission from Spring Security.

  The Inventory cluster is the core, everything revolves around it. InventoryIn should have linkage with reasons of purchases. 
  And InventoryOut are each defined by its purpose: SALES? Input for MANUFACTURING? GIFT OUT? WAREHOUSE TRANSFERRING (from A address to B address of the same or even different companies)
  I used @ManyToMany annotation to express these relationships currently. 
- Second are the Repository Interfaces which extends Jpa or lower CRUD Repository, taking advantage of Spring ready-baked codes, to interact  directly with the database layers.
- Thirdly are Services classes, which represented most of the application Model in the MVC.
I implemented the Quantity.class, used to handle all logic related to calculating Floating point numbers, in order to ensure the decimals did not get distorted.
I do not know if my approach was correct or not, but in most of my logic, I added a List<String> results in each argument list of methods in these Service classes.
I wonder if those List<String> results should be replaced by proper Exceptions and appropriate handling methods (and Views)...
- Next were Controllers that connect our Model to the Views that is fed to users.
- Finally were the Views, written using HTML, CSS and Javascript. I try practicing many ways to present data to webpages, but all of them use a single technik: Grid, Grids all the way, because I love Grid...and Flex containers
