/**
 * https://project-osrm.org/docs/v5.5.1/api/#general-options
 * Uses OSRM (Routing Machine) API to find the fastest route from "start" coordinates to "end" coordinates.
 * @param req
 * @returns {Promise<Response>}
 * @constructor
 */
export async function GET(req) {
    const { searchParams } = new URL(req.url);
    const start = searchParams.get("start"); // lon,lat
    const end = searchParams.get("end");     // lon,lat

    const osrmUrl = `https://router.project-osrm.org/route/v1/driving/${start};${end}?overview=full&geometries=geojson`;

    try {
        const res = await fetch(osrmUrl);
        const data = await res.json();
        return Response.json(data);
    } catch (err) {
        return Response.json({ error: "Failed to fetch route" }, { status: 500 });
    }
}