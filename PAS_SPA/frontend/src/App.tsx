import {BrowserRouter, Routes, Route, Link, Navigate} from 'react-router-dom';
import {ToastContainer} from 'react-toastify';
import UserList from './pages/UserList';
import UserForm from './pages/UserForm';
import UserDetails from './pages/UserDetails';
import ReservationList from './pages/ReservationList';
import ReservationForm from './pages/ReservationForm';

function App() {
    return (
        <BrowserRouter>
            <div className="container">
                <nav>
                    <h1>System rezerwacji boisk sportowych PADAN</h1>
                    <Link to="/users">Klienci</Link>
                    <Link to="/reservations">Rezerwacje</Link>
                </nav>

                <Routes>
                    <Route path="/" element={<Navigate to="/users" replace/>}/>
                    <Route path="/users" element={<UserList/>}/>
                    <Route path="/users/new" element={<UserForm/>}/>
                    <Route path="/users/edit/:id" element={<UserForm/>}/>
                    <Route path="/users/:id" element={<UserDetails/>}/>
                    <Route path="/reservations" element={<ReservationList/>}/>
                    <Route path="/reservations/new" element={<ReservationForm/>}/>
                </Routes>

                <ToastContainer/>
            </div>
        </BrowserRouter>
    );
}

export default App;