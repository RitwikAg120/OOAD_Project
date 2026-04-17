# 🏥 MediTrack - Medicine Expiry & Home Inventory Tracker

A complete web application for tracking medicine expiry dates, managing home inventories, and setting reminders.

## 📋 Project Structure

```
OOAD Project/
├── pom.xml                                    # Maven build configuration
├── src/
│   ├── main/
│   │   ├── java/com/medicinetracker/
│   │   │   ├── MedicineTrackerApplication.java    # Spring Boot main class
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java            # Login/Logout API
│   │   │   │   ├── MedicineController.java        # Medicine CRUD API
│   │   │   │   ├── InventoryController.java       # Inventory API
│   │   │   │   ├── ReminderController.java        # Reminder API
│   │   │   │   └── NotificationController.java    # Notification API
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── UserDTO.java
│   │   │   │   ├── MedicineDTO.java
│   │   │   │   ├── InventoryDTO.java
│   │   │   │   └── ApiResponse.java
│   │   │   └── config/
│   │   ├── resources/
│   │   │   ├── application.properties         # Server configuration
│   │   │   └── static/
│   │   │       └── index.html                 # Frontend UI
│   │   └── test/
└── ritwik/, rohit/, ritesh/, rishikesh/     # Original Java classes
```

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Installation & Running

1. **Build the project:**
   ```bash
   cd 'C:\coding\OOAD Project'
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - Open browser: `http://localhost:8080`
   - The app will load with the login page

### Demo Credentials

**Admin Account:**
- Email: `ritwik@admin.com`
- Password: `1234`
- Role: Admin (Full access to all features)

**Regular Users:**
- `rishi@home.com` / `1234`
- `rohit@home.com` / `rohit789`
- `ritesh@home.com` / `ritesh000`

## ✨ Features

### 🔐 Authentication
- ✅ User login & logout
- ✅ Role-based access control (Admin vs Regular User)
- ✅ Session management

### 📊 Dashboard
- ✅ Overview statistics (Total medicines, Expired, Expiring Soon, Low Stock)
- ✅ Near-expiry medicines list (30 days)
- ✅ Active reminders display
- ✅ Real-time data from API

### 💊 Medicine Management
- ✅ **Add New Medicine** - Create medicines with category, batch, expiry date
- ✅ **View All Medicines** - Complete inventory list with status
- ✅ **Edit Medicine** - Update quantity, expiry date, etc.
- ✅ **Delete Medicine** - Remove from inventory
- ✅ **Search Medicines** - By name, category, batch number
- ✅ **Status Indicators** - Expired, Expiring Soon, Low Stock, OK

### 📅 Reminders
- ✅ **Create Reminders** - Set frequency (Once, Daily, Weekly, Monthly)
- ✅ **View All Reminders** - Active and inactive reminders
- ✅ **Cancel Reminders** - Deactivate reminders
- ✅ **Auto-remind** - For medicines expiring soon

### 📋 Reports
- ✅ **Near Expiry Report** - Medicines expiring within 30 days
- ✅ **Low Stock Report** - Medicines below minimum stock level
- ✅ **Admin Audit Report** - Complete system overview

### 👥 Admin Features
- ✅ **System Statistics** - Overall metrics
- ✅ **Audit View** - All medicines across all users
- ✅ **User Management** - View all users
- ✅ **Critical Alerts** - Expired and low-stock medicines

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/validate` - Validate token
- `GET /api/auth/user` - Get current user info

### Medicines
- `GET /api/medicines` - Get all medicines
- `GET /api/medicines/{id}` - Get medicine by ID
- `POST /api/medicines` - Add new medicine
- `PUT /api/medicines/{id}` - Update medicine
- `DELETE /api/medicines/{id}` - Delete medicine
- `GET /api/medicines/search/name/{keyword}` - Search by name
- `GET /api/medicines/search/category/{category}` - Search by category
- `GET /api/medicines/expiring/{days}` - Get medicines expiring in N days
- `GET /api/medicines/low-stock` - Get low-stock medicines

### Reminders
- `GET /api/reminders` - Get all reminders
- `GET /api/reminders/active` - Get active reminders
- `POST /api/reminders` - Create reminder
- `DELETE /api/reminders/{id}` - Delete reminder
- `PUT /api/reminders/{id}/cancel` - Cancel reminder

### Notifications
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/unread` - Get unread notifications
- `PUT /api/notifications/{id}/read` - Mark as read
- `DELETE /api/notifications/{id}` - Delete notification

## 🎨 UI Features

- **Responsive Design** - Works on desktop, tablet, and mobile
- **Modern Interface** - Gradient backgrounds, smooth animations
- **Real-time Updates** - Data loads from API
- **Modal Dialogs** - For adding/editing medicines and reminders
- **Status Badges** - Color-coded status indicators
- **Loading States** - Spinners while fetching data
- **Toast Notifications** - Success/error messages

## 🔄 Data Flow

1. **User logs in** → Credentials validated → JWT token generated
2. **Dashboard loads** → API fetches medicines, reminders, statistics
3. **User adds medicine** → Modal form → API POST → UI updates
4. **Admin views audit** → API returns all medicines across system
5. **Reminders trigger** → Notifications created → User notified

## 💾 Database

- **Type:** H2 (In-memory)
- **Access:** http://localhost:8080/h2-console
- **Auto-reset:** On each application start (development mode)

## 🛠️ Technologies Used

- **Backend:** Spring Boot 3.0.0
- **Frontend:** HTML5, CSS3, JavaScript (Vanilla)
- **Database:** H2 In-memory Database
- **Build Tool:** Maven
- **Security:** Session-based authentication

## 📝 Next Steps for Production

1. Replace H2 with PostgreSQL/MySQL
2. Implement JWT token validation
3. Add Spring Security authentication
4. Enable database persistence
5. Add unit tests
6. Deploy to cloud (AWS, Azure, Heroku)

## 📞 Support

For issues or questions, please refer to the project documentation or contact the development team.

---

**Team:** Ritwik | Ritesh | Rohit | Rishikesh  
**Project:** OOAD Medicine Tracker Application
