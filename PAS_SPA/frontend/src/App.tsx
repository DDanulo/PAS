import {BrowserRouter, Link, Navigate, Route, Routes} from 'react-router-dom';
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import AuthProvider, {useAuth} from './context/LoggedUserContext';
import ProtectedRoute, {RoleEnum} from './HandleProtection';

import UserList from './pages/UserList';
import UserMe from './pages/UserMe';
import UserCreateForm from './pages/UserCreateForm';
import UserDetails from './pages/UserDetails';
import UserEditForm from "./pages/UserEditForm";
import ReservationList from './pages/ReservationList';
import ReservationForm from './pages/ReservationForm';
import LoginForm from './pages/LoginForm';
import MyReservationList from "./pages/MyReservations.tsx";

const Bar = () => {
    const {isAuthenticated, logout, userLogin, userRole} = useAuth();

    return (
        <nav className="navbar">
            <h1 className="navbar-brand">System rezerwacji boisk PADAN</h1>

            <div className="navbar-menu">
                {isAuthenticated ? (
                    <>
                        {userRole === RoleEnum.ADMIN ?
                            <div className="navbar-links">
                                <Link to="/users" className="nav-link">Zarządzaj użytkownikami</Link>
                                <Link to="/reservations" className="nav-link">Rezerwacje</Link>
                            </div> :
                            <div className="navbar-links">
                                <Link to="/me" className="nav-link">Moje konto</Link>
                                <Link to="/me/reservations" className="nav-link">Moje rezerwacje</Link>
                            </div>
                        }

                        <div className="navbar-user">
                            <span className="user-label">
                                Zalogowany: <strong>{userLogin}</strong> ({userRole})
                            </span>
                            <button onClick={() => logout()} className="btn-logout">
                                Wyloguj
                            </button>
                        </div>
                    </>
                ) : (
                    <Link to="/login" className="nav-link">Zaloguj się</Link>
                )}
            </div>
        </nav>
    );
};

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <div className="app-container">
                    <Bar/>

                    <div className="content-container">
                        <Routes>
                            <Route path="/login" element={<LoginForm/>}/>

                            <Route path="/users" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.ADMIN]}>
                                    <UserList/>
                                </ProtectedRoute>
                            }/>
                            <Route path="/me" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.CLIENT, RoleEnum.ADMIN]}>
                                    <UserMe/>
                                </ProtectedRoute>
                            }/>
                            <Route path="/me/edit" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.CLIENT, RoleEnum.ADMIN]}>
                                    <UserEditForm/>
                                </ProtectedRoute>
                            }/>
                            <Route path="/me/reservations" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.CLIENT, RoleEnum.ADMIN]}>
                                    <MyReservationList/>
                                </ProtectedRoute>
                            }/>

                            <Route path="/users/new" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.ADMIN]}>
                                    <UserCreateForm/>
                                </ProtectedRoute>
                            }/>

                            <Route path="/users/edit/:id" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.ADMIN]}>
                                    <UserEditForm/>
                                </ProtectedRoute>
                            }/>

                            <Route path="/users/:id" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.ADMIN]}>
                                    <UserDetails/>
                                </ProtectedRoute>
                            }/>

                            <Route path="/me/details" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.ADMIN, RoleEnum.CLIENT]}>
                                    <UserDetails/>
                                </ProtectedRoute>
                            }/>

                            <Route path="/reservations" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.ADMIN]}>
                                    <ReservationList/>
                                </ProtectedRoute>
                            }/>

                            <Route path="/reservations/new" element={
                                <ProtectedRoute allowedRoles={[RoleEnum.CLIENT, RoleEnum.ADMIN]}>
                                    <ReservationForm/>
                                </ProtectedRoute>
                            }/>

                            <Route path="/" element={<Navigate to="/users" replace/>}/>
                        </Routes>
                    </div>

                    <ToastContainer/>
                </div>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;