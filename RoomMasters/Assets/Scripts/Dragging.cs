using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Dragging : MonoBehaviour
{
    float distance = 0;
    bool dragging = false;
    Vector3 offset;
    Transform toDrag;

    public GameObject cube;

    // Update is called once per frame
    void Update()
    {
        Vector3 v3;

        var position = Camera.main.ScreenToWorldPoint(Input.mousePosition);


        if (Input.touchCount != 1)
        {
            dragging = false;
            return;
        }

        Touch touch = Input.touches[0];
        Vector3 pos = touch.position;
        Ray ray = Camera.main.ScreenPointToRay(pos);
        RaycastHit hit;
        if (touch.phase == TouchPhase.Began)
        {
            if (Physics.Raycast(ray, out hit))
            {
                var point = hit.point;
                var vectorChange = new Vector3(point.x, point.y + cube.transform.localScale.y / 2, point.z);
                cube.transform.position = vectorChange;
                dragging = true;
            }
        }

        if (dragging && touch.phase == TouchPhase.Moved)
        {
            if (Physics.Raycast(ray, out hit))
            {
                var point = hit.point;
                var vectorChange = new Vector3(point.x, point.y + cube.transform.localScale.y / 2, point.z);
                cube.transform.position = vectorChange;
                dragging = true;
            }
        }

        if (dragging && (touch.phase == TouchPhase.Ended || touch.phase == TouchPhase.Canceled))
        {
            dragging = false;
        }

    }
}
