import {useEffect, useState} from 'react';
import axiosSetup from '../api/axiosSetup.ts';
import {Link} from 'react-router-dom';
import {toast} from 'react-toastify';

export default function ReservationList() {
    const [reservations, setReservations] = useState([]);
    const [clients, setClients] = useState<any[]>([]);
    const [rooms, setRooms] = useState<any[]>([]);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [resRes, resClients, resRooms] = await Promise.all([
                axiosSetup.get('/reservations'),
                axiosSetup.get('/users'),
                axiosSetup.get('/rooms')
            ]);
            setReservations(resRes.data);
            setClients(resClients.data);
            setRooms(resRooms.data);
        } catch (e) {
            console.error(e);
            toast.error("Błąd podczas pobierania rezerwacji");
        }
    };

    const formatDate = (dateVal: any) => {
        if (!dateVal) return "";
        if (Array.isArray(dateVal)) {
            const [year, month, day, hour, minute] = dateVal;
            return new Date(year, month - 1, day, hour || 0, minute || 0).toLocaleString();
        }
        return new Date(dateVal).toLocaleString();
    };

    const endReservation = async (id: string) => {
        if (!id) return;
        if (!window.confirm("Czy na pewno chcesz zakończyć rezerwację?")) return;

        try {
            await axiosSetup.post(`/reservations/${id}/end`);
            toast.success("Zakończono rezerwację");
            loadData();
        } catch (e: any) {
            console.error("Błąd podczas kończenia rezerwacji:", e);
            const msg = e.response?.data?.message || "Nie udało się zakończyć rezerwacji";
            toast.error(msg);
        }
    };

    const getClientName = (id: string) => {
        const client = clients.find((c: any) => c.id === id);
        return client ? `${client.firstName} ${client.lastName} (${client.login})` : id;
    };

    const getRoomName = (id: string) => {
        const room = rooms.find((r: any) => r.id === id);
        return room ? `${room.roomType} (Cena: ${room.basePrice})` : id;
    };

    return (
        <div className="container">
            <h2>Lista wszystkch rezerwacji</h2>
            <Link to="/reservations/new" className="btn">
                Dodaj nową rezerwację
            </Link>

            <table style={{width: '100%', borderCollapse: 'collapse'}}>
                <thead>
                <tr>
                    <th>Dane klienta (imie, nazwisko, login)</th>
                    <th>Boisko</th>
                    <th>Data startu rezerwacji</th>
                    <th>Data końca rezerwacji</th>
                    <th>Zakończ rezerwację</th>
                </tr>
                </thead>
                <tbody>
                {reservations.map((res: any) => {
                    const currentId = res.reservationId;

                    return (
                        <tr key={currentId}>
                            <td>{getClientName(res.clientId)}</td>
                            <td>{getRoomName(res.roomId)}</td>

                            <td>{formatDate(res.startTime)}</td>

                            <td>
                                {res.endTime ? formatDate(res.endTime) :
                                    <span style={{color: '#4caf50', fontWeight: 'bold'}}>AKTYWNA</span>}
                            </td>

                            <td>
                                {!res.endTime && (
                                    <button onClick={() => endReservation(currentId)} className="btn">
                                        Zakończ
                                    </button>
                                )}
                            </td>
                        </tr>
                    );
                })}
                </tbody>
            </table>
        </div>
    );
}