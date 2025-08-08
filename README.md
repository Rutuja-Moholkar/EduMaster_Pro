# EduMaster Pro 🎓

A modern, full-stack e-learning platform built with Spring Boot and React, designed to provide comprehensive online education solutions.

## 🚀 Features

### 📚 Course Management
- Course creation and management for instructors
- Rich course content with videos, quizzes, and assignments
- Course categories and levels (Beginner, Intermediate, Advanced)
- Course search and filtering capabilities

### 👥 User Management
- Multi-role authentication (Student, Instructor, Admin)
- JWT-based secure authentication
- User profiles and dashboard
- Role-based access control

### 💳 Payment Integration
- Stripe payment processing
- Course enrollment and payment tracking
- Revenue analytics for instructors
- Refund management

### 🏆 Learning Features
- Progress tracking and completion certificates
- Interactive quizzes and assessments
- Discussion forums and community features
- Mobile-responsive design

### 📊 Analytics & Reporting
- Student progress analytics
- Instructor revenue dashboards
- Course performance metrics
- Comprehensive reporting system

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT
- **Payment**: Stripe API
- **Documentation**: OpenAPI 3.0 (Swagger)
- **Migration**: Flyway
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18
- **Language**: TypeScript
- **UI Library**: Material-UI (MUI)
- **Routing**: React Router v6
- **State Management**: Redux Toolkit
- **Styling**: CSS-in-JS with MUI theming
- **Build Tool**: Create React App

### Development Tools
- **Version Control**: Git
- **IDE**: VS Code / IntelliJ IDEA
- **API Testing**: Postman / Swagger UI
- **Package Managers**: Maven (Backend), npm (Frontend)

## 📋 Prerequisites

- **Java 17+**
- **Node.js 18+**
- **MySQL 8.0+**
- **Maven 3.6+**
- **Git**

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/Rutuja-Moholkar/EduMaster_Pro.git
cd EduMaster_Pro
```

### 2. Database Setup
```bash
# Connect to MySQL
mysql -u root -p

# Create database and user
CREATE DATABASE edumaster_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'edumaster_user'@'localhost' IDENTIFIED BY 'edumaster_password';
GRANT ALL PRIVILEGES ON edumaster_db.* TO 'edumaster_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Backend Setup
```bash
# Navigate to backend directory
cd backend

# Install dependencies and run
mvn clean install -DskipTests
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

Backend will be available at: `http://localhost:8082/api/v1`

### 4. Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will be available at: `http://localhost:3000`

## 📁 Project Structure

```
EduMaster_Pro/
├── backend/                    # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/edumaster/
│   │   │   │   ├── controller/     # REST Controllers
│   │   │   │   ├── service/        # Business Logic
│   │   │   │   ├── repository/     # Data Access Layer
│   │   │   │   ├── model/          # Entity Classes
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── security/       # Security Configuration
│   │   │   │   └── util/           # Utility Classes
│   │   │   └── resources/
│   │   │       ├── application.yml # Configuration
│   │   │       └── db/migration/   # Flyway Scripts
│   │   └── test/                   # Unit Tests
│   └── pom.xml                     # Maven Configuration
├── frontend/                   # React Frontend
│   ├── public/                     # Static Assets
│   ├── src/
│   │   ├── components/             # Reusable Components
│   │   ├── pages/                  # Page Components
│   │   ├── hooks/                  # Custom Hooks
│   │   ├── services/               # API Services
│   │   ├── utils/                  # Utility Functions
│   │   └── styles/                 # Styling Files
│   └── package.json                # npm Configuration
├── docs/                       # Documentation
│   ├── SETUP-GUIDE.md             # Complete Setup Guide
│   └── Technical-Documentation.md  # Technical Details
└── README.md                   # This file
```

## 🔗 API Endpoints

### Authentication
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Token refresh

### Courses
- `GET /api/v1/courses/public` - Get all published courses
- `GET /api/v1/courses/public/{id}` - Get course by ID
- `POST /api/v1/courses` - Create course (Instructor+)
- `PUT /api/v1/courses/{id}` - Update course (Instructor+)

### Enrollments
- `POST /api/v1/enrollments` - Enroll in course
- `GET /api/v1/enrollments/user/{id}` - Get user enrollments
- `PUT /api/v1/enrollments/{id}/progress` - Update progress

### Payments
- `POST /api/v1/payments/create-intent` - Create payment intent
- `POST /api/v1/payments/confirm` - Confirm payment
- `GET /api/v1/payments/user/{id}` - Get user payments

## 🎨 UI Features

### Design System
- **Theme**: Modern educational design with light grey color scheme
- **Colors**: Professional blue (#3B82F6) and green (#10B981) accents
- **Typography**: Clear, readable fonts with proper hierarchy
- **Responsive**: Mobile-first responsive design

### Key Pages
- **Home**: Hero section with featured courses and statistics
- **Courses**: Course catalog with search and filters
- **Course Detail**: Comprehensive course information and enrollment
- **Dashboard**: User-specific dashboard with progress tracking
- **Certificates**: Digital certificates for completed courses

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access**: Student, Instructor, and Admin roles
- **Password Encryption**: BCrypt password hashing
- **CORS Configuration**: Proper cross-origin request handling
- **Input Validation**: Comprehensive input sanitization

## 💾 Database Schema

The application uses MySQL with Flyway migrations for version control:

- **Users**: User accounts and profiles
- **Courses**: Course information and content
- **Categories**: Course categorization
- **Enrollments**: Student course enrollments
- **Payments**: Payment transactions
- **Notifications**: System notifications

## 🧪 Testing

### Backend Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
```

### Frontend Testing
```bash
# Run tests
npm test

# Run tests with coverage
npm test -- --coverage
```

## 📝 Documentation

- **Setup Guide**: [SETUP-GUIDE.md](docs/SETUP-GUIDE.md) - Complete setup instructions
- **Technical Docs**: [Technical-Documentation.md](docs/Technical-Documentation.md) - Detailed technical documentation
- **API Docs**: Available at `http://localhost:8082/swagger-ui.html` when backend is running

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Rutuja Moholkar** - *Initial work* - [Rutuja-Moholkar](https://github.com/Rutuja-Moholkar)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React team for the powerful frontend library
- Material-UI team for the beautiful components
- Stripe for payment processing capabilities

## 📞 Support

For support and questions:
- Create an issue in the [GitHub repository](https://github.com/Rutuja-Moholkar/EduMaster_Pro/issues)
- Check the [Setup Guide](docs/SETUP-GUIDE.md) for common issues

---

⭐ **If you found this project helpful, please give it a star!** ⭐