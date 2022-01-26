USE supermarketDB;
DROP TABLE IF EXISTS `StoresOrderedItems`;
CREATE TABLE `StoresOrderedItems` (
`OrderID` varchar(255) NOT NULL,
`ItemID` INT NOT NULL,
`CustomerID` INT NOT NULL,
  `StoreID` INT NOT NULL,
  `NumOfItems` INT NOT NULL,
  `Price` Double NOT NULL,
  `Date` date,
  `Status` varchar(255) NOT NULL,
  PRIMARY KEY (`OrderID`,`ItemID`)
);
