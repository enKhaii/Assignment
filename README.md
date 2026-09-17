# CourierPro
Object-Oriented Programming Techniques - Assignment
<br>
This repository contains an academic assignment. It is made public for portfolio and demonstration purposes only.


# About The Project
A console-based Java system for managing end-to-end courier and logistics operations. Built for a university OOP module, models a real-world delivery business — senders create and pay for shipments, admins assign drivers and manage the fleet, couriers pick up and deliver packages, and anyone can publicly track a shipment by ID. The project focuses on applying core OOP principles (inheritance, polymorphism, encapsulation, abstraction, composition, aggregation) to a non-trivial, multi-role business domain rather than a toy example.

# Tech Stack
Language: Java<br>
Paradigm: Object-Oriented Programming (three-tier architecture)<br>
Data Storage: In-memory collections (ArrayList) with seed data for demonstration<br>
Interface: Console-based text UI

# Key Features
Multi-role access control — separate portals for Admin, Courier, and Sender, each with role-appropriate permissions. Admins and couriers authenticate with credentials; senders use lightweight ID-based access with no password, matching real-world walk-in customer flows

Fleet management — full vehicle CRUD, maintenance scheduling with due-date tracking, and vehicle–courier assignment that keeps both objects synchronized bidirectionally

Shipment lifecycle tracking — 8-status workflow (PENDING_PAYMENT → PAID → PICKED_UP → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED, plus FAILED_ATTEMPT / CANCELLED) with a full timestamped history log per shipment

Automated fee calculation — computes shipping cost from base weight rate, distance, and a declared-value-based insurance fee, with Standard/Express speed tiers

Business rule enforcement — couriers cannot pick up, update, or deliver shipments without a vehicle assigned; shipments must follow valid status transitions; vehicles under maintenance can't be assigned

Public tracking — anyone can track a shipment by tracking ID with no login required

# System Architecture
Three-tier design separating concerns across 17 files:<br>
Presentation Layer -> FleetManagement, ShipmentManagement, SenderManagement, CourierPortal, QuickTrack<br>
Business Logic Layer -> FleetManager, ShipmentRegistry, UserRegistry<br>
Data Layer -> Person(abstract), Admin/Courier/Sender, Vehicle, Shipment(owns Parcel)

# OOP Concepts Demonstrated
Inheritance - Person abstract class with Admin, Courier, Sender subclasses<br>
Polymorphhism - displayInfo() overridden per subclasses<br>
Encapsulation - Private fields throughout, controlled via getters/setters<br>
Abstraction - Abstract Person.displayInfo() forces subclasses implementation<br>
Composition - Shipment owns Parcel (lifecycle-bound)<br>
Aggregation - FleetManager manages Vehicle(s), ShipmentRegistry manages Shipment(s)<br>
Association - Bidirectional Vehicle - Courier reference

# Team
Built by a 3-person team, each owning a full feature area

1. System Architecture, Fleet & Admin Integration
- Three-tier architecture and Main.java navigation layer
- Fleet Management module, Vehicle, FleetManager, FleetManagement(CRUD), maintenance scheduling, vehicle-courier sync
- Public Quick Track, no-auth shipment tracking
- Admin features, registration flows, driver-to-shipment assignment, delivery failure handling

2. Courier Operations & Parcel
- Person, abstract base classes establishing the shared inheritance hierarchy for Admin, Courier, Sender
- UserRegistry, user storage and authentication across all roles
- Courier & CourierPortal, driver-facing console UI for daily delivery operations
- Parcel, package details(content type, weight, dimensions, declared value) composed within each Shipment

3. Sender & Shipment
- Sender entity, SenderManagement, self-service, no-login/ID-based registration
- Shipment creation wizard, payment processing, shipment cancellation logic
- Shipment Management module, ShipmentRegistry, ShipmentManagement, fee engine, 8-status tracking, history logging
- System documentation, UML diagrams, workflow
