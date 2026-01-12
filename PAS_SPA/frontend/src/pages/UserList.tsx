import {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import axiosSetup from '../api/axiosSetup.ts';
import {toast} from 'react-toastify';

export default function UserList() {
    const [users, setUsers] = useState<User[]>([]);
    const [search, setSearch] = useState('');
    const navigate = useNavigate();

    const fetchUsers = async () => {
        try {
            const url = search ? `/users/search?login=${search}` : '/users';
            const res = await axiosSetup.get(url);
            setUsers(res.data.map((u: any) => ({...u, id: u.id || u.userId})));
        } catch (e) {
            toast.error("Błąd podczas pobierania listy uźytkowników");
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const toggleStatus = async (id: string, isActive: boolean) => {
        if (!window.confirm("Czy chcesz na pewno zmienić status uźytkownika?")) return;
        try {
            await axiosSetup.post(`/users/${id}/${isActive ? 'deactivate' : 'activate'}`);
            fetchUsers();
            toast.success("Zmieniono status uźytkownika");
        } catch (e) {
            toast.error("Nie udało się zmienić statusu uźytkownika");
        }
    };

    return (
        <div>
            <h2>Dostępni użytkownicy</h2>
            <div>
                <input placeholder="Wprowadź login" value={search} onChange={e => setSearch(e.target.value)}/>
                <button className="btn" onClick={fetchUsers}>Szukaj</button>
                <button className="btn" onClick={() => navigate('/users/new')}>Dodaj Klienta</button>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Login</th>
                    <th>Imię</th>
                    <th>Nazwisko</th>
                    <th>Email</th>
                    <th>Status konta</th>
                    <th>Dostępne opcje</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.login}</td>
                        <td>{user.firstName}</td>
                        <td>{user.lastName}</td>
                        <td>{user.email}</td>
                        <td>{user.isActive ? 'AKTYWNY' : 'NIEAKTYWNY'}</td>
                        <td>
                            <Link to={`/users/${user.id}`} className="btn">Szczegóły konta</Link>
                            <Link to={`/users/edit/${user.id}`} className="btn">Edytuj dane</Link>
                            <button className="btn" onClick={() => toggleStatus(user.id!, user.isActive)}>
                                {user.isActive ? 'Dezaktywuj konto' : 'Aktywuj konto'}
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}