import axios from 'axios'

const roleApi = axios.create({
    baseURL: 'https://api.kurobbs.com',
    headers: {
        'source': 'android',
        'Content-Type': 'application/x-www-form-urlencoded',
        'Accept': 'application/json, text/plain, */*'
    }
})

export const getRoleData = (bAt, roleId, serverId) => {
    const params = new URLSearchParams()
    params.append('roleId', roleId)
    params.append('serverId', serverId)
    return roleApi.post('/aki/roleBox/akiBox/roleData', params, {
        headers: {
            'b-at': bAt
        }
    })
}

export const getRoleDetail = (bAt, roleId, serverId, id) => {
    const params = new URLSearchParams()
    params.append('roleId', roleId)
    params.append('serverId', serverId)
    params.append('id', id)
    return roleApi.post('/aki/roleBox/akiBox/getRoleDetail', params, {
        headers: {
            'b-at': bAt
        }
    })
}