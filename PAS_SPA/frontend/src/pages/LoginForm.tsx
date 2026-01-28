import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import {useAuth} from '../context/LoggedUserContext';
import {RoleEnum} from "../HandleProtection.tsx";

const LoginForm = () => {
    const {setTokens, isAuthenticated, userRole} = useAuth();
    const navigate = useNavigate();

    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    useEffect(() => {
        if (isAuthenticated && userRole) {
            if (userRole === RoleEnum.ADMIN) {
                navigate('/users');
            } else {
                navigate('/me');
            }
        }
    }, [isAuthenticated, userRole, navigate]);
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            const response = await axios.post('http://localhost:8080/PAS_SPA/api/v1/auth/login', {
                login: login,
                password: password
            });

            const {accessToken, refreshToken} = response.data;

            setTokens(accessToken, refreshToken);
        } catch (err: any) {
            console.error("Login Error:", err);

            if (err.response) {
                switch (err.response.status) {
                    case 401:
                        setError("Błędne hasło lub login.");
                        break;
                    case 403:
                        setError("Konto nie jest aktywne w systemie.");
                        break;
                    default:
                        setError("Błąd serwera. Spróbuj ponownie później.");
                }
            } else {
                setError("Brak połączenia z serwerem.");
            }
        }
    };

    return (
        <div className="container">
            <div className="login-box">
                <h2>Logowanie</h2>
                {error && <div className="error-message" style={{color: 'red', marginBottom: '10px'}}>{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <input
                            type="text"
                            placeholder="Login"
                            value={login}
                            onChange={e => setLogin(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="password"
                            placeholder="Hasło"
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="login-button">Zaloguj</button>
                </form>
            </div>
        </div>
    );
};

export default LoginForm;