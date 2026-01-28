import {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {toast} from 'react-toastify';
import axiosSetup from '../api/axiosSetup';
import {useAuth} from '../context/LoggedUserContext';
import {RoleEnum} from '../HandleProtection';

export default function UserList() {
    const [users, setUsers] = useState<any[]>([]);
    const [search, setSearch] = useState('');
    const navigate = useNavigate();
    const {userRole, userLogin} = useAuth();

    const fetchUsers = async () => {
        try {
            const url = search ? `/users/search?login=${search}` : '/users';
            const res = await axiosSetup.get(url);

            let data = res.data;
            if (!Array.isArray(data)) data = [data];

            if (userRole === RoleEnum.CLIENT) {
                data = data.filter((u: any) => u.login === userLogin);
            }

            setUsers(data.map((u: any) => ({...u, id: u.id || u.userId})));
        } catch (e) {
            console.error(e);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, [userRole]);

    const toggleStatus = async (id: string, isActive: boolean) => {
        if (!window.confirm("Czy na pewno chcesz zmienić status?")) return;
        try {
            await axiosSetup.post(`/users/${id}/${isActive ? 'deactivate' : 'activate'}`);
            fetchUsers();
            toast.success("Zmieniono status");
        } catch (e) {
            toast.error("Błąd zmiany statusu");
        }
    };

    return (
        <div className="container">
            <div className="list-header">
                <h2>{userRole === RoleEnum.ADMIN ? "Zarządzanie użytkownikami" : "Moje dane"}</h2>

                {userRole === RoleEnum.ADMIN && (
                    <div className="search-box">
                        <input
                            className="form-control"
                            placeholder="Szukaj loginu..."
                            value={search}
                            onChange={e => setSearch(e.target.value)}
                        />
                        <button className="btn" onClick={fetchUsers}>Szukaj</button>
                        <button className="btn btn-primary" onClick={() => navigate('/users/new')}>Dodaj Klienta
                        </button>
                    </div>
                )}
            </div>

            <table className="data-table">
                <thead>
                <tr>
                    <th>Login</th>
                    <th>Imię</th>
                    <th>Nazwisko</th>
                    <th>Email</th>
                    <th>Status</th>
                    <th>Opcje</th>
                </tr>
                </thead>
                <tbody>
                {users.length > 0 ? (
                    users.map(user => (
                        <tr key={user.id}>
                            <td>{user.login}</td>
                            <td>{user.firstName}</td>
                            <td>{user.lastName}</td>
                            <td>{user.email}</td>
                            <td className={user.isActive ? 'status-active' : 'status-blocked'}>
                                {user.isActive ? 'AKTYWNY' : 'ZABLOKOWANY'}
                            </td>
                            <td className="actions-cell">

                                <Link to={`/users/${user.id}`}className="link-details">
                                    Szczegóły
                                </Link>

                                <Link to={`/users/edit/${user.id}`} className="link-edit">
                                    Edytuj
                                </Link>

                                {/*{userRole === RoleEnum.ADMIN && (*/}
                                <button
                                    className={`btn-small ${user.isActive ? 'btn-danger' : 'btn-success'}`}
                                    onClick={() => toggleStatus(user.id!, user.isActive)}
                                >
                                    {user.isActive ? 'Zablokuj' : 'Aktywuj'}
                                </button>
                                {/*)}*/}
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan={6} className="empty-message">
                            Brak użytkowników.
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}