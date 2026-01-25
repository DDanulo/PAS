import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';
import {useAuth} from '../context/LoggedUserContext';

const LoginForm = () => {
    const {setToken} = useAuth();
    const navigate = useNavigate();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            const response = await axios.post('http://localhost:8080/PAS_SPA/api/v1/auth/login', {
                login: login,
                password: password
            });

            const token = response.data;
            setToken(token);
            navigate('/users');
        } catch (err: any) {
            console.error(err);
            if (err.response && err.response.status === 401) {
                setError("Błędny hasło lub login");
            } else if (err.response && err.response.status === 403) {
                setError("Konto nie jest aktywne w systemie.");
            } else {
                setError("Wystąpił nieoczekiwany błąd");
            }
        }
    };

    return (
        <div className="container">
            <h2>Logowanie</h2>
            {error && <div className="error-message">{error}</div>}

            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Login"
                    value={login}
                    onChange={e => setLogin(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Hasło"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                />
                <button type="submit">Zaloguj</button>
            </form>
        </div>
    );
};

export default LoginForm;